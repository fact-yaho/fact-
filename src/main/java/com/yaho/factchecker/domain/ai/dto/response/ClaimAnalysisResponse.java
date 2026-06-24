package com.yaho.factchecker.domain.ai.dto.response;

import com.yaho.factchecker.domain.ai.dto.common.ExtractedClaim;
import java.util.List;

public record ClaimAnalysisResponse(
        String originalText,
        List<ExtractedClaim> claims
) {
}
