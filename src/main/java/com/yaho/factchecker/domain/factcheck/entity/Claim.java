package com.yaho.factchecker.domain.factcheck.entity;

import com.yaho.factchecker.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Table(name = "claim")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Claim extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "claim_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "fact_check_id", columnDefinition = "uuid", nullable = false)
    private UUID factCheckId;

    @Column(name = "claim_analysis_ai_log_id", columnDefinition = "uuid")
    private UUID claimAnalysisAiLogId;

    @Column(name = "original_text", columnDefinition = "TEXT", nullable = false)
    private String originalText;

    @Column(name = "canonical_claim", columnDefinition = "TEXT", nullable = false)
    private String canonicalClaim;

    @Column(name = "time_scope", length = 255)
    private String timeScope;

    @Column(name = "is_verifiable", nullable = false)
    private boolean verifiable;

    @Column(name = "unverifiable_reason", columnDefinition = "TEXT")
    private String unverifiableReason;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaimCategoryMapping> categories = new ArrayList<>();

    @Builder
    private Claim(
            UUID factCheckId,
            UUID claimAnalysisAiLogId,
            String originalText,
            String canonicalClaim,
            String timeScope,
            boolean verifiable,
            String unverifiableReason
    ) {
        this.factCheckId = factCheckId;
        this.claimAnalysisAiLogId = claimAnalysisAiLogId;
        this.originalText = originalText;
        this.canonicalClaim = canonicalClaim;
        this.timeScope = timeScope;
        this.verifiable = verifiable;
        this.unverifiableReason = unverifiableReason;
    }

    public void addCategory(ClaimCategoryMapping categoryMapping) {
        this.categories.add(categoryMapping);
        categoryMapping.assignClaim(this);
    }
}
