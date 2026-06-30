package com.yaho.factchecker.domain.retrieval.service;

import com.yaho.factchecker.global.exception.BusinessException;
import com.yaho.factchecker.global.exception.ErrorCode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TextEmbedder {

    private static final int EMBEDDING_DIMENSION = 1536;

    private final EmbeddingModel embeddingModel;
    private final String model;

    public TextEmbedder(
            // Spring AI에서 제공되는 OpenAI 임베딩 API 호출 도구
            EmbeddingModel embeddingModel,
            // 모델명 주입
            @Value("${ai.openai.model.embedding}") String model
    ) {
        this.embeddingModel = embeddingModel;
        this.model = model;
    }

    /**
     * 텍스트 1개를 임베딩 벡터로 변환
     *
     * text = 임베딩할 텍스트 (소주장, fact 등)
     * 최종 반환 결과 = 임베딩 벡터 (small 모델 기준 1536차원)
     */
    public float[] embed(String text) {
        if (text == null || text.isBlank()) {
            throw new BusinessException(ErrorCode.AI_EMPTY_RESPONSE);
        }
        try {
            EmbeddingRequest request = new EmbeddingRequest(
                    List.of(text),
                    OpenAiEmbeddingOptions.builder().model(model).build());

            // API 호출된 결과에서 첫번째 결과(=float[])를 가져옴
            float[] output = embeddingModel.call(request).getResults().get(0).getOutput();

            if (output.length != EMBEDDING_DIMENSION) {
                log.error("임베딩 차원 불일치: expected={}, actual={} (model={})",
                        EMBEDDING_DIMENSION, output.length, model);
                throw new BusinessException(ErrorCode.AI_API_CALL_FAILED);
            }
            return output;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.AI_API_CALL_FAILED, e);
        }
    }
}
