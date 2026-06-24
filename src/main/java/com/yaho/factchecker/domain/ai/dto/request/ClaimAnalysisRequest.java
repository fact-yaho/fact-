package com.yaho.factchecker.domain.ai.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ClaimAnalysisRequest(
        @NotBlank(message = "분석할 사용자 입력은 필수입니다.")
        String input
) {
}
