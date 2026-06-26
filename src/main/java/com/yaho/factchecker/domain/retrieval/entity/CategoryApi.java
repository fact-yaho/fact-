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
@Table(name = "category_api")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryApi {
    @Id
    @UuidGenerator
    @Column(name = "category_api_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID categoryApiId;

    @Column(name = "api_name", length = 255)
    private String apiName;

    // FK, category_id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", columnDefinition = "uuid", nullable = false)
    private Category category;

    @Builder
    public CategoryApi(String apiName, Category category) {
        this.apiName = apiName;
        this.category = category;
    }
}
