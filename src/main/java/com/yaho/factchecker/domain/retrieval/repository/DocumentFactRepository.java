package com.yaho.factchecker.domain.retrieval.repository;

import com.yaho.factchecker.domain.retrieval.entity.DocumentFact;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentFactRepository extends JpaRepository<DocumentFact, UUID> {

    List<DocumentFact> findAllByEvidenceDocument_EvidenceDocumentId(UUID evidenceDocumentId);

    /*
     * [근거 문서 검색 — fact 단위 벡터 유사도]
     * 소주장 벡터와 각 세부 사실(fact_vector)의 유사도를 계산해
     * 문서 단위로 집계 → rerank 입력으로 사용.
     * 역시 pgvector <=> 네이티브 쿼리가 들어갈 자리.
     *
     * 예) 입력 소주장 벡터와 가까운 fact Top-N (문서 id 포함해 집계용으로 조회):
     * SELECT df.*, (df.fact_vector <=> :queryVector) AS distance
     *   FROM p_document_fact df
     *   JOIN p_evidence_document ed ON df.evidence_document_id = ed.evidence_document_id
     *  WHERE ed.claim_id = :claimId
     *    AND df.deleted_at IS NULL
     *  ORDER BY df.fact_vector <=> :queryVector
     *  LIMIT :topN
     */
}
