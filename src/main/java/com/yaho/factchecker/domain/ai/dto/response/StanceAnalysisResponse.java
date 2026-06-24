package com.yaho.factchecker.domain.ai.dto.response;

import com.yaho.factchecker.domain.ai.type.Stance;
import java.util.List;

public record StanceAnalysisResponse(
        String canonicalClaim,
        List<EvidenceStanceResultResponse> evidences,
        Stance representativeStance
) {
}
