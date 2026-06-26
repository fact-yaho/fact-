package com.yaho.factchecker.domain.retrieval.dto;

import java.util.UUID;

/**
 * BM25 계산 결과를 저장 (단위 = 문서)
 * evidenceDocumentId = 근거문서 id
 * score = BM25 raw 점수 (클수록 관련성 높음)
 * rank = BM25 순위 (1부터, 점수 내림차순)
 */
public record Bm25Result(
        UUID evidenceDocumentId,
        double score,
        int rank
) {}
