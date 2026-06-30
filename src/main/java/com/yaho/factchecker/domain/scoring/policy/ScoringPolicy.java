package com.yaho.factchecker.domain.scoring.policy;

import com.yaho.factchecker.global.type.Stance;
import com.yaho.factchecker.global.type.Verdict;
import org.springframework.stereotype.Component;

/**
 * 점수 계산 정책을 모아둔 클래스
 */
@Component
public class ScoringPolicy {

    private static final String SCORING_VERSION = "v1.0";

    private static final double MIN_RELEVANCE_SCORE = 0.5;
    private static final double MIN_SIMILARITY_SCORE = 0.1;

    private static final int MIN_VALID_EVIDENCE_COUNT = 1;

    public String scoringVersion() {
        return SCORING_VERSION;
    }

    public int minValidEvidenceCount() {
        return MIN_VALID_EVIDENCE_COUNT;
    }

    public boolean isScorableEvidence(
        Stance stance,
        double relevanceScore,
        double similarityScore
    ) {
        if (stance == null) {
            return false;
        }

        if (!isDirectionalStance(stance)) {
            return false;
        }

        return relevanceScore >= MIN_RELEVANCE_SCORE
            && similarityScore >= MIN_SIMILARITY_SCORE;
    }

    public boolean isDirectionalStance(Stance stance) {
        return stance == Stance.SUPPORTS
            || stance == Stance.PARTIAL
            || stance == Stance.CONTRADICTS;
    }

    public double stanceWeight(Stance stance) {
        return switch (stance) {
            case SUPPORTS -> 1.0;
            case PARTIAL -> 0.45;
            case CONTRADICTS -> -1.0;
            case INSUFFICIENT, IRRELEVANT -> 0.0;
        };
    }

    public Verdict determineVerdict(int finalScore) {
        if (finalScore >= 85) {
            return Verdict.CONSISTENT;
        }

        if (finalScore >= 60) {
            return Verdict.PARTIALLY_CONSISTENT;
        }

        if (finalScore >= 40) {
            return Verdict.UNCERTAIN;
        }

        if (finalScore >= 15) {
            return Verdict.PARTIALLY_INCONSISTENT;
        }

        return Verdict.INCONSISTENT;
    }
}
