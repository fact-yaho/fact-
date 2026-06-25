package com.yaho.factchecker.domain.retrieval.entity;

import com.pgvector.PGvector;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@Table(name = "p_document_fact")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentFact {

    @Id
    @UuidGenerator
    @Column(name = "document_fact_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID documentFactId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evidence_document_id", columnDefinition = "uuid", nullable = false)
    private EvidenceDocument evidenceDocument;

    @Column(name = "fact_text", columnDefinition = "TEXT")
    private String factText;

    @JdbcTypeCode(SqlTypes.OTHER)
    // 만약 모델 차원 변경 시 vector(1536) 값 변경 필요
    @Column(name = "fact_vector", columnDefinition = "vector(1536)")
    private PGvector factVector;

    @Builder
    public DocumentFact(EvidenceDocument evidenceDocument, String factText, PGvector factVector) {
        this.evidenceDocument = evidenceDocument;
        this.factText = factText;
        this.factVector = factVector;
    }
}
