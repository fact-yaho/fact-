package com.yaho.factchecker.domain.ai.dto.response;

import com.yaho.factchecker.domain.ai.type.Stance;

public record EvidenceStanceResultResponse(
        String sourceDocumentId, // 판단 대상이 된 근거자료 ID
        Stance stance, // 해당 근거자료가 주장을 우리가 정해놓은 5단계 중 어디에 해당되는지
        String reason // 판단 이유
) {
}
