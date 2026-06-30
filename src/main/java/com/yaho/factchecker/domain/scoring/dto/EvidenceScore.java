package com.yaho.factchecker.domain.scoring.dto;

import com.yaho.factchecker.global.type.Stance;
import java.util.UUID;

/**
 * 근거별 계산 결과 DTO
 * @param evidenceId
 * @param stance
 * @param relevanceScore
 * @param similarityScore
 * @param evidenceWeight
 * @param stanceWeight
 * @param contribution
 * @param valid
 */
public record EvidenceScore(
    UUID evidenceId,
    Stance stance,
    double relevanceScore,
    double similarityScore,
    // evidenceWeight = relevanceScore x similarityScore
    double evidenceWeight,
    // stance에 따른 방향성 가중치
    double stanceWeight,
    // contribution = evidenceWeight x stanceWeight
    double contribution,
    // 최종 계산에 포함된 근거인지 여부
    boolean valid
) {

}
