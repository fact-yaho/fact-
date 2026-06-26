package com.yaho.factchecker.domain.retrieval.repository.projection;

import java.util.UUID;

// 유사 질문 탐색용
public interface SimilarClaimProjection {
    // 기존(유사) 소주장의 claim_id
    UUID getClaimId();
    // 코사인 유사도 (1 - 거리), 클수록 유사
    Double getSimilarity();
}
