package com.yaho.factchecker.domain.retrieval.repository;

import com.yaho.factchecker.domain.ai.type.ClaimCategory;
import com.yaho.factchecker.domain.retrieval.entity.Category;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByCategoryName(ClaimCategory categoryName);

    Optional<Category> findByCategoryCode(Integer categoryCode);
}
