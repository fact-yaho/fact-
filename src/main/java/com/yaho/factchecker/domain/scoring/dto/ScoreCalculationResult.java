package com.yaho.factchecker.domain.scoring.dto;

import com.yaho.factchecker.global.type.Verdict;
import java.util.List;

/**
 * 최종 계산 결과 DTO
 * @param finalScore
 * @param verdict
 * @param normalizedScore
 * @param totalEvidenceCount
 * @param validEvidenceCount
 * @param evidenceScores
 * @param scoringVersion
 */
public record ScoreCalculationResult(
    Integer finalScore,
    Verdict verdict,
    Double normalizedScore,
    int totalEvidenceCount,
    int validEvidenceCount,
    List<EvidenceScore> evidenceScores,
    String scoringVersion
) {

    /**
     * 0점과 근거부족을 구분하기 위해 추가한 매서드 입니다.<br>
     * 근거부족일때의 finalScore = null
     */
    public static ScoreCalculationResult insufficient (
        int totalEvidenceCount,
        List<EvidenceScore> evidenceScores,
        String scoringVersion
    ){
        return new ScoreCalculationResult(
            null,
            Verdict.INSUFFICIENT,
            null,
            totalEvidenceCount,
            0,
            evidenceScores,
            scoringVersion
        );
    }
}
