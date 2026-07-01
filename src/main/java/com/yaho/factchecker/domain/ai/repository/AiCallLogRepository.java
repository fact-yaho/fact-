package com.yaho.factchecker.domain.ai.repository;

import com.yaho.factchecker.domain.ai.entity.AiCallLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiCallLogRepository extends JpaRepository<AiCallLog, UUID> {

}
