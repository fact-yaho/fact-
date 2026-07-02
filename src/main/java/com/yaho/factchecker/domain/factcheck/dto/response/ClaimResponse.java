package com.yaho.factchecker.domain.factcheck.dto.response;

import java.util.List;
import java.util.UUID;

public record ClaimResponse(
        UUID claimId,
        UUID factCheckId,
        UUID claimAnalysisAiLogId,
        String originalText,
        String canonicalClaim,
        String timeScope,
        boolean verifiable,
        String unverifiableReason,
        List<ClaimCategoryResponse> categories
) {
}
