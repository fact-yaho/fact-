package com.yaho.factchecker.infrastructure.ai.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaho.factchecker.application.ai.port.StanceAnalysisPort;
import com.yaho.factchecker.domain.ai.dto.command.AiCallLogFailureCommand;
import com.yaho.factchecker.domain.ai.dto.command.AiCallLogSuccessCommand;
import com.yaho.factchecker.domain.ai.dto.request.StanceAnalysisRequest;
import com.yaho.factchecker.domain.ai.dto.response.StanceAnalysisResponse;
import com.yaho.factchecker.domain.ai.service.AiCallLogService;
import com.yaho.factchecker.domain.ai.type.AiCallType;
import com.yaho.factchecker.global.exception.BusinessException;
import com.yaho.factchecker.global.exception.ErrorCode;
import com.yaho.factchecker.infrastructure.ai.prompt.PromptLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAiStanceAnalysisAdapter implements StanceAnalysisPort {

    private static final String PROMPT_PATH = "prompts/ai/stance-analysis-system.txt";

    private final ChatClient chatClient;
    private final PromptLoader promptLoader;
    private final ObjectMapper objectMapper;
    private final String model;
    private final AiCallLogService aiCallLogService;

    public OpenAiStanceAnalysisAdapter(
            ChatClient.Builder chatClientBuilder,
            PromptLoader promptLoader,
            ObjectMapper objectMapper,
            @Value("${ai.openai.model.stance-analysis}") String model,
            AiCallLogService aiCallLogService
    ) {
        this.chatClient = chatClientBuilder.build();
        this.promptLoader = promptLoader;
        this.objectMapper = objectMapper;
        this.model = model;
        this.aiCallLogService = aiCallLogService;
    }

    @Override
    public StanceAnalysisResponse analyze(StanceAnalysisRequest request) {
        String systemPrompt = promptLoader.load(PROMPT_PATH);
        long startTime = System.currentTimeMillis();

        String requestJson;

        try {
            requestJson = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            saveFailure(
                    request.toString(),
                    null,
                    ErrorCode.AI_REQUEST_SERIALIZE_FAILED,
                    e.getMessage(),
                    startTime
            );
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
            saveFailure(
                    requestJson,
                    null,
                    ErrorCode.AI_API_CALL_FAILED,
                    e.getMessage(),
                    startTime
            );
            throw new BusinessException(ErrorCode.AI_API_CALL_FAILED, e);
        }

        if (content == null || content.isBlank()) {
            saveFailure(
                    requestJson,
                    content,
                    ErrorCode.AI_EMPTY_RESPONSE,
                    "AI 응답이 비어있습니다.",
                    startTime
            );
            throw new BusinessException(ErrorCode.AI_EMPTY_RESPONSE);
        }

        try {
            StanceAnalysisResponse response =
                    objectMapper.readValue(content, StanceAnalysisResponse.class);

            saveSuccess(requestJson, content, startTime);

            return response;
        } catch (JsonProcessingException e) {
            saveFailure(
                    requestJson,
                    content,
                    ErrorCode.AI_RESPONSE_PARSE_FAILED,
                    e.getMessage(),
                    startTime
            );
            throw new BusinessException(ErrorCode.AI_RESPONSE_PARSE_FAILED, e);
        }
    }

    private void saveSuccess(String inputData, String outputData, long startTime) {
        aiCallLogService.saveSuccess(AiCallLogSuccessCommand.builder()
                .callType(AiCallType.STANCE_ANALYSIS)
                .modelName(model)
                .promptPath(PROMPT_PATH)
                .promptVersion(null)
                .inputData(inputData)
                .outputData(outputData)
                .latencyMs(System.currentTimeMillis() - startTime)
                .build());
    }

    private void saveFailure(
            String inputData,
            String outputData,
            ErrorCode errorCode,
            String errorMessage,
            long startTime
    ) {
        aiCallLogService.saveFailure(AiCallLogFailureCommand.builder()
                .callType(AiCallType.STANCE_ANALYSIS)
                .modelName(model)
                .promptPath(PROMPT_PATH)
                .promptVersion(null)
                .inputData(inputData)
                .outputData(outputData)
                .errorCode(errorCode.getCode())
                .errorMessage(errorMessage)
                .latencyMs(System.currentTimeMillis() - startTime)
                .build());
    }
}
