package com.yaho.factchecker.domain.factcheck.repository;

import com.yaho.factchecker.domain.factcheck.entity.Claim;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, UUID> {

}
