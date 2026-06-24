package com.yaho.factchecker.domain.ai.dto.request;

import com.yaho.factchecker.domain.ai.dto.common.ExtractedClaim;
import java.util.List;

public record StanceAnalysisRequest(
        ExtractedClaim claim,
        List<EvidenceForStanceRequest> evidences
) {
}
