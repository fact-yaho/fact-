package com.yaho.factchecker.domain.retrieval.dto;

import com.yaho.factchecker.global.type.ClaimCategory;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * relevanceScore = BM25 정규화된 점수 (0~1 사이)
 * similarityScore = 벡터 유사도 (0~1 사이)
 * finalScore = RRF 점수
 */
public record RetrievedEvidence(
        // 문서 정보
        UUID evidenceDocumentId,
        String title,
        String contentCleaned,
        String apiName,
        String searchKeyword,
        ClaimCategory categoryName,
        LocalDateTime publishedAt,
        String originalUrl,
        String authorOrDept,

        // 문서 점수 및 순위
        double relevanceScore,
        int bm25Rank,
        double similarityScore,
        int vectorSimRank,
        double finalScore,
        int finalRank
) {}
