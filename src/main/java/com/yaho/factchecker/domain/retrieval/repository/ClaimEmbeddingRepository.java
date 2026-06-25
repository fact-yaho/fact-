package com.yaho.factchecker.domain.retrieval.repository;

import com.yaho.factchecker.domain.retrieval.entity.ClaimEmbedding;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimEmbeddingRepository extends JpaRepository<ClaimEmbedding, UUID> {

    // 소주장(claim) 기준 임베딩 조회
    Optional<ClaimEmbedding> findByClaimId(UUID claimId);

    /*
     * [유사질문(중복) 검사 — 벡터 유사도]
     * pgvector 코사인 거리(<=>) 기반 Top-K 검색이 들어갈 자리.
     * JPQL 표현 불가 → @Query(nativeQuery = true) 로 작성 예정.
     *
     * 예) 같은 카테고리 내에서 입력 소주장 벡터와 가장 가까운 기존 소주장 N개:
     * SELECT * FROM p_claim_embedding
     *  WHERE category_id = :categoryId
     *    AND deleted_at IS NULL
     *  ORDER BY claim_vector <=> :queryVector
     *  LIMIT :topK
     *
     * 차원수/거리연산자(<=> 코사인, <-> L2, <#> 내적)/인덱스(ivfflat·hnsw)
     * 확정 후 구현예정. 파라미터로 PGvector 바인딩 방식도 같이 정함.
     */
}
