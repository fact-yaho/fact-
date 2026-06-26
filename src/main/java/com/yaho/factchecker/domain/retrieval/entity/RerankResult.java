package com.yaho.factchecker.domain.retrieval.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
@Table(name = "rerank_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RerankResult {

    @Id
    @UuidGenerator
    @Column(name = "rerank_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID rerankId;

    // FK
    @Column(name = "claim_id", columnDefinition = "uuid", nullable = false)
    private UUID claimId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evidence_document_id", columnDefinition = "uuid", nullable = false)
    private EvidenceDocument evidenceDocument;

    @Column(name = "bm25_rank", nullable = false)
    private Integer bm25Rank;

    @Column(name = "vector_sim_rank", nullable = false)
    private Integer vectorSimRank;

    @Column(name = "vector_sim_score", nullable = false)
    private Double vectorSimScore;

    @Column(name = "final_rank", nullable = false)
    private Integer finalRank;

    @Builder
    public RerankResult(UUID claimId, EvidenceDocument evidenceDocument, Integer bm25Rank,
                        Integer vectorSimRank, Double vectorSimScore, Integer finalRank) {
        this.claimId = claimId;
        this.evidenceDocument = evidenceDocument;
        this.bm25Rank = bm25Rank;
        this.vectorSimRank = vectorSimRank;
        this.vectorSimScore = vectorSimScore;
        this.finalRank = finalRank;
    }
}
