package com.yaho.factchecker.domain.retrieval.repository;

import com.yaho.factchecker.domain.retrieval.entity.RerankResult;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RerankResultRepository extends JpaRepository<RerankResult, UUID> {

    // 특정 소주장의 재정렬 결과를 최종 순위 오름차순으로 (Top-K 추출용)
    List<RerankResult> findAllByClaimIdOrderByFinalRankAsc(UUID claimId);

    // 재계산 시 기존 결과 덮어쓰기를 위한 hard delete (추후 BaseEntity 상속하면 softDelete로)
    void deleteByClaimId(UUID claimId);
}
