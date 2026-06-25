package com.yaho.factchecker.domain.retrieval.repository;

import com.yaho.factchecker.domain.retrieval.entity.ClaimEmbedding;
import com.yaho.factchecker.domain.retrieval.repository.projection.SimilarClaimProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClaimEmbeddingRepository extends JpaRepository<ClaimEmbedding, UUID> {

    // 소주장(claim) 기준 임베딩 단건 조회
    Optional<ClaimEmbedding> findByClaimId(UUID claimId);

    /**
     * [유사질문 검사] 입력 벡터와 가장 가까운 기존 소주장 Top-K.
     * - 정렬: 코사인 거리(<=>) 오름차순  → HNSW 인덱스 사용
     * - 반환: 코사인 유사도(1 - 거리)     → 컬럼 의미(sim_score)와 일치
     * - threshold(동일질문 판정 기준)는 service에서 적용
     *
     * @param queryVector pgvector 리터럴 문자열 "[v1,v2,...]"  (VectorUtils.toVectorLiteral)
     * @param limit       가져올 개수 (K=5)
     */
    @Query(value = """
            SELECT
                ce.claim_id AS claimId,
                1 - (ce.claim_vector <=> CAST(:queryVector AS vector)) AS similarity
            FROM claim_embedding ce
            WHERE ce.claim_vector IS NOT NULL
            ORDER BY ce.claim_vector <=> CAST(:queryVector AS vector)
            LIMIT :limit
            """, nativeQuery = true)
    List<SimilarClaimProjection> findSimilarClaims(
            @Param("queryVector") String queryVector,
            @Param("limit") int limit
    );
}
