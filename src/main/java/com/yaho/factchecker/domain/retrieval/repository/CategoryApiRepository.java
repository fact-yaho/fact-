package com.yaho.factchecker.domain.retrieval.repository;

import com.yaho.factchecker.domain.retrieval.entity.CategoryApi;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryApiRepository extends JpaRepository<CategoryApi, UUID> {

    List<CategoryApi> findAllByCategory_CategoryId(UUID categoryId);
}
