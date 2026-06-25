package com.yaho.factchecker.infrastructure.ai.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaho.factchecker.application.ai.port.StanceAnalysisPort;
import com.yaho.factchecker.domain.ai.dto.request.StanceAnalysisRequest;
import com.yaho.factchecker.domain.ai.dto.response.StanceAnalysisResponse;
import com.yaho.factchecker.infrastructure.ai.prompt.PromptLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class OpenAiStanceAnalysisAdapter implements StanceAnalysisPort {

    private final ChatClient chatClient;
    private final PromptLoader promptLoader;
    private final ObjectMapper objectMapper;

    public OpenAiStanceAnalysisAdapter(
            ChatClient.Builder chatClientBuilder,
            PromptLoader promptLoader,
            ObjectMapper objectMapper
    ) {
        this.chatClient = chatClientBuilder.build();
        this.promptLoader = promptLoader;
        this.objectMapper = objectMapper;
    }

    @Override
    public StanceAnalysisResponse analyze(StanceAnalysisRequest request) {
        String systemPrompt = promptLoader.load("prompts/ai/stance-analysis-system.txt");

        try {
            String requestJson = objectMapper.writeValueAsString(request);

            String content = chatClient.prompt()
                    .system(systemPrompt)
                    .user(requestJson)
                    .call()
                    .content();

            if (content == null || content.isBlank()) {
                throw new IllegalStateException("AI stance 분석 응답이 비어 있습니다.");
            }

            return objectMapper.readValue(content, StanceAnalysisResponse.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("AI stance 분석 요청/응답을 처리할 수 없습니다.", e);
        }
    }
}
