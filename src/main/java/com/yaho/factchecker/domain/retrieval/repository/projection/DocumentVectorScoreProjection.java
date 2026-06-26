package com.yaho.factchecker.domain.retrieval.repository.projection;

import java.util.UUID;

// 문서 내 fact 중 최고 유사도
public interface DocumentVectorScoreProjection {
    // 근거문서 id
    UUID getEvidenceDocumentId();
    // 해당 문서 fact 중 최대 코사인 유사도
    Double getMaxSimilarity();
}
