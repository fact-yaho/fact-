package com.yaho.factchecker.domain.retrieval.dto;

import java.util.UUID;

/**
 * 벡터 유사도 계산 결과 (문서 단위).
 * evidenceDocumentId = 근거문서 id
 * score = 코사인 유사도 (1 - 거리, fact 중 MAX). 클수록 유사
 * rank = 유사도 순위 (1부터, 유사도 내림차순)
 */
public record VectorResult(
        UUID evidenceDocumentId,
        double score,
        int rank
) {}
