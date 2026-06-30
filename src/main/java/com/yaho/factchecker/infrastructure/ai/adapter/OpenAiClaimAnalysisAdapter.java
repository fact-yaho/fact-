package com.yaho.factchecker.infrastructure.ai.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaho.factchecker.application.ai.port.ClaimAnalysisPort;
import com.yaho.factchecker.domain.ai.dto.request.ClaimAnalysisRequest;
import com.yaho.factchecker.domain.ai.dto.response.ClaimAnalysisResponse;
import com.yaho.factchecker.global.exception.BusinessException;
import com.yaho.factchecker.global.exception.ErrorCode;
import com.yaho.factchecker.infrastructure.ai.prompt.PromptLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAiClaimAnalysisAdapter implements ClaimAnalysisPort {

    private final ChatClient chatClient;
    private final PromptLoader promptLoader;
    private final ObjectMapper objectMapper;
    private final String model;

    public OpenAiClaimAnalysisAdapter(
            ChatClient.Builder chatClientBuilder,
            PromptLoader promptLoader,
            ObjectMapper objectMapper,
            @Value("${ai.openai.model.claim-analysis}") String model
    ) {
        this.chatClient = chatClientBuilder.build();
        this.promptLoader = promptLoader;
        this.objectMapper = objectMapper;
        this.model = model;
    }

    @Override
    public ClaimAnalysisResponse analyze(ClaimAnalysisRequest request) {
        String systemPrompt = promptLoader.load("prompts/ai/claim-analysis-system.txt");

        String content;

        try {
            content = chatClient.prompt()
                    .options(OpenAiChatOptions.builder()
                            .model(model)
                            .build())
                    .system(systemPrompt)
                    .user(request.input())
                    .call()
                    .content();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.AI_API_CALL_FAILED, e);
        }

        if (content == null || content.isBlank()) {
            throw new BusinessException(ErrorCode.AI_EMPTY_RESPONSE);
        }

        try {
            return objectMapper.readValue(content, ClaimAnalysisResponse.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_PARSE_FAILED, e);
        }

    }
}
