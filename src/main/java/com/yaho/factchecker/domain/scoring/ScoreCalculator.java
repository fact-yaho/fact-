package com.yaho.factchecker.domain.scoring;

import com.yaho.factchecker.domain.scoring.dto.EvidenceJudgment;
import com.yaho.factchecker.domain.scoring.dto.ScoreCalculationResult;
import java.util.List;

// Orchestrator는 이 인터페이스만 바라보면 됩니다.
public interface ScoreCalculator {

    ScoreCalculationResult calculate(List<EvidenceJudgment> evidences);
}
