package com.yaho.factchecker.domain.ai.dto.command;

import com.yaho.factchecker.domain.ai.type.AiCallType;
import lombok.Builder;

@Builder
public record AiCallLogSuccessCommand(
        AiCallType callType,
        String modelName,
        String promptPath,
        String promptVersion,
        String inputData,
        String outputData,
        Long latencyMs
) {
}
