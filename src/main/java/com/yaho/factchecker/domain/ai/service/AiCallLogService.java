package com.yaho.factchecker.domain.ai.service;

import com.yaho.factchecker.domain.ai.dto.command.AiCallLogFailureCommand;
import com.yaho.factchecker.domain.ai.dto.command.AiCallLogSuccessCommand;
import com.yaho.factchecker.domain.ai.entity.AiCallLog;
import com.yaho.factchecker.domain.ai.repository.AiCallLogRepository;
import com.yaho.factchecker.domain.ai.type.AiCallStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiCallLogService {

    private final AiCallLogRepository aiCallLogRepository;

    public void saveSuccess(AiCallLogSuccessCommand command) {
        AiCallLog aiCallLog = AiCallLog.builder()
                .callType(command.callType())
                .modelName(command.modelName())
                .promptPath(command.promptPath())
                .promptVersion(command.promptVersion())
                .inputData(command.inputData())
                .outputData(command.outputData())
                .status(AiCallStatus.SUCCESS)
                .latencyMs(command.latencyMs())
                .build();

        try {
            aiCallLogRepository.save(aiCallLog);
        } catch (Exception e) {
            log.warn("AI 호출 로그 저장 실패", e);
        }
    }

    public void saveFailure(AiCallLogFailureCommand command) {
        AiCallLog aiCallLog = AiCallLog.builder()
                .callType(command.callType())
                .modelName(command.modelName())
                .promptPath(command.promptPath())
                .promptVersion(command.promptVersion())
                .inputData(command.inputData())
                .outputData(command.outputData())
                .status(AiCallStatus.FAILED)
                .errorCode(command.errorCode())
                .errorMessage(command.errorMessage())
                .latencyMs(command.latencyMs())
                .build();

        try {
            aiCallLogRepository.save(aiCallLog);
        } catch (Exception e) {
            log.warn("AI 호출 로그 저장 실패", e);
        }
    }
}
