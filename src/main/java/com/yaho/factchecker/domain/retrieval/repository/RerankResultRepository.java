package com.yaho.factchecker.domain.retrieval.repository;

import com.yaho.factchecker.domain.retrieval.entity.RerankResult;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RerankResultRepository extends JpaRepository<RerankResult, UUID> {

    List<RerankResult> findAllByClaimIdOrderByFinalRankAsc(UUID claimId);
}
