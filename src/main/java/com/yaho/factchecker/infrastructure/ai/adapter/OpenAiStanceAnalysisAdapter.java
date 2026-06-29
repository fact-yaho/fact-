package com.yaho.factchecker.infrastructure.ai.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaho.factchecker.application.ai.port.StanceAnalysisPort;
import com.yaho.factchecker.domain.ai.dto.request.StanceAnalysisRequest;
import com.yaho.factchecker.domain.ai.dto.response.StanceAnalysisResponse;
import com.yaho.factchecker.global.exception.BusinessException;
import com.yaho.factchecker.global.exception.ErrorCode;
import com.yaho.factchecker.infrastructure.ai.prompt.PromptLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAiStanceAnalysisAdapter implements StanceAnalysisPort {

    private final ChatClient chatClient;
    private final PromptLoader promptLoader;
    private final ObjectMapper objectMapper;
    private final String model;

    public OpenAiStanceAnalysisAdapter(
            ChatClient.Builder chatClientBuilder,
            PromptLoader promptLoader,
            ObjectMapper objectMapper,
            @Value("${ai.openai.model.stance-analysis}") String model
    ) {
        this.chatClient = chatClientBuilder.build();
        this.promptLoader = promptLoader;
        this.objectMapper = objectMapper;
        this.model = model;
    }

    @Override
    public StanceAnalysisResponse analyze(StanceAnalysisRequest request) {
        String systemPrompt = promptLoader.load("prompts/ai/stance-analysis-system.txt");

        String requestJson;

        try {
            requestJson = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.AI_REQUEST_SERIALIZE_FAILED, e);
        }

        String content;

        try {
            content = chatClient.prompt()
                    .options(OpenAiChatOptions.builder()
                            .model(model)
                            .build())
                    .system(systemPrompt)
                    .user(requestJson)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.AI_API_CALL_FAILED, e);
        }

        if (content == null || content.isBlank()) {
            throw new BusinessException(ErrorCode.AI_EMPTY_RESPONSE);
        }

        try {
            return objectMapper.readValue(content, StanceAnalysisResponse.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_PARSE_FAILED, e);
        }
    }
}
