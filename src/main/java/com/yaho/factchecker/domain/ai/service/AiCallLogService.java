package com.yaho.factchecker.domain.ai.service;

import com.yaho.factchecker.domain.ai.dto.command.AiCallLogFailureCommand;
import com.yaho.factchecker.domain.ai.dto.command.AiCallLogSuccessCommand;
import com.yaho.factchecker.domain.ai.entity.AiCallLog;
import com.yaho.factchecker.domain.ai.repository.AiCallLogRepository;
import com.yaho.factchecker.domain.ai.type.AiCallStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiCallLogService {

    private final AiCallLogRepository aiCallLogRepository;

    public void saveSuccess(AiCallLogSuccessCommand command) {
        AiCallLog log = AiCallLog.builder()
                .callType(command.callType())
                .modelName(command.modelName())
                .promptPath(command.promptPath())
                .promptVersion(command.promptVersion())
                .inputData(command.inputData())
                .outputData(command.outputData())
                .status(AiCallStatus.SUCCESS)
                .latencyMs(command.latencyMs())
                .build();

        aiCallLogRepository.save(log);
    }

    public void saveFailure(AiCallLogFailureCommand command) {
        AiCallLog log = AiCallLog.builder()
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

        aiCallLogRepository.save(log);
    }
}
