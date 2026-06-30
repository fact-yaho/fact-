package com.yaho.factchecker.domain.scoring.dto;

import com.yaho.factchecker.global.type.Stance;
import java.util.UUID;

/**
 * 근거별 판단 결과 DTO
 * @param evidenceId
 * @param stance
 * @param relevanceScore 검색된 공식자료가 사용자 주장과 얼마나 관련이 있는지를 나타내는 점수
 * @param similarityScore 사용자 주장과 공식 자료 내용의 의미적으로 얼마나 가까운지를 나타내는 점수<br>
 *                        주의) "주장을 지지하는 정도"를 의미하는 점수 아님(이 부분은 stance가 담당), 비교 대상이 될 만큼 의미적으로 가까운지를 보는 값
 */
public record EvidenceJudgment(
    UUID evidenceId,
    Stance stance,
    double relevanceScore,
    double similarityScore
) {

    /**
     * relevanceScore, similarityScore의 범위가 0.0~1.0일때 사용합니다.
     * @param evidenceId
     * @param stance
     * @param relevanceScore
     * @param similarityScore
     * @return {@link EvidenceJudgment}
     */
    public static EvidenceJudgment of(
        UUID evidenceId,
        Stance stance,
        double relevanceScore,
        double similarityScore
    ) {
        return new EvidenceJudgment(
            evidenceId,
            stance,
            relevanceScore,
            similarityScore
        );
    }

    /**
     * relevanceScore, similarityScore의 범위가 0~100일때 사용합니다.
     * @param evidenceId
     * @param stance
     * @param relevancePercent
     * @param similarityPercent
     * @return {@link EvidenceJudgment}
     */
    public static EvidenceJudgment ofPercent(
        UUID evidenceId,
        Stance stance,
        double relevancePercent,
        double similarityPercent
    ) {
        return new EvidenceJudgment(
            evidenceId,
            stance,
            relevancePercent / 100.0,
            similarityPercent / 100.0
        );
    }
}
