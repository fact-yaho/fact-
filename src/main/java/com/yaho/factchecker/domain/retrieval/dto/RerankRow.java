package com.yaho.factchecker.domain.retrieval.dto;

import java.util.UUID;

/**
 * RRF 융합 최종 결과 (문서 단위). rerank_result 저장에 필요한 모든 값 포함.
 * evidenceDocumentId = 근거문서 id
 * bm25Score = BM25 raw 점수
 * bm25Rank = BM25 순위
 * vectorScore = 벡터 유사도 점수
 * vectorRank = 벡터 순위
 * finalRank = RRF 융합 최종 순위 (1부터)
 */
public record RerankRow(
        UUID evidenceDocumentId,
        double bm25Score,
        int bm25Rank,
        double vectorScore,
        int vectorRank,
        int finalRank
) {}
