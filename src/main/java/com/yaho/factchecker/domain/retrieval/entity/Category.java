package com.yaho.factchecker.domain.retrieval.entity;

import com.yaho.factchecker.domain.ai.type.ClaimCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @UuidGenerator
    @Column(name = "category_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID categoryId;

    @Column(name = "category_code", nullable = false, unique = true)
    private Integer categoryCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name", nullable = false)
    private ClaimCategory categoryName;

    @Builder
    public Category(Integer categoryCode, ClaimCategory categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }
}
