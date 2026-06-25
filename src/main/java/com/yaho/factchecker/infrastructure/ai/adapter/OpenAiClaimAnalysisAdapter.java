package com.yaho.factchecker.infrastructure.ai.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaho.factchecker.application.ai.port.ClaimAnalysisPort;
import com.yaho.factchecker.domain.ai.dto.request.ClaimAnalysisRequest;
import com.yaho.factchecker.domain.ai.dto.response.ClaimAnalysisResponse;
import com.yaho.factchecker.infrastructure.ai.prompt.PromptLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class OpenAiClaimAnalysisAdapter implements ClaimAnalysisPort {

    private final ChatClient chatClient;
    private final PromptLoader promptLoader;
    private final ObjectMapper objectMapper;

    public OpenAiClaimAnalysisAdapter(
            ChatClient.Builder chatClientBuilder,
            PromptLoader promptLoader,
            ObjectMapper objectMapper
    ) {
        this.chatClient = chatClientBuilder.build();
        this.promptLoader = promptLoader;
        this.objectMapper = objectMapper;
    }

    @Override
    public ClaimAnalysisResponse analyze(ClaimAnalysisRequest request) {
        String systemPrompt = promptLoader.load("prompts/ai/claim-analysis-system.txt");

        String content = chatClient.prompt()
                .system(systemPrompt)
                .user(request.input())
                .call()
                .content();

        try {
            return objectMapper.readValue(content, ClaimAnalysisResponse.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("AI claim 분석 응답을 파싱할 수 없습니다.", e);
        }

    }

}
