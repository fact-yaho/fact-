package com.yaho.factchecker.domain.scoring.service;

import com.yaho.factchecker.domain.scoring.ScoreCalculator;
import com.yaho.factchecker.domain.scoring.dto.EvidenceJudgment;
import com.yaho.factchecker.domain.scoring.dto.EvidenceScore;
import com.yaho.factchecker.domain.scoring.dto.ScoreCalculationResult;
import com.yaho.factchecker.domain.scoring.policy.ScoringPolicy;
import com.yaho.factchecker.global.type.Stance;
import com.yaho.factchecker.global.type.Verdict;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 실제 계산 로직
 */
@Component
@RequiredArgsConstructor
public class DefaultScoreCalculator implements ScoreCalculator {

    private final ScoringPolicy scoringPolicy;

    @Override
    public ScoreCalculationResult calculate(List<EvidenceJudgment> evidences) {
        List<EvidenceJudgment> safeEvidences = evidences == null
            ? List.of()
            : evidences;

        if (safeEvidences.isEmpty()) {
            return ScoreCalculationResult.insufficient(
                0,
                List.of(),
                scoringPolicy.scoringVersion()
            );
        }

        validateEvidences(safeEvidences);

        List<EvidenceScore> evidenceScores = safeEvidences.stream()
            .map(this::calculateEvidenceScore)
            .toList();

        List<EvidenceScore> validEvidenceScores = evidenceScores.stream()
            .filter(EvidenceScore::valid)
            .toList();

        if (validEvidenceScores.size() < scoringPolicy.minValidEvidenceCount()) {
            return ScoreCalculationResult.insufficient(
                evidenceScores.size(),
                evidenceScores,
                scoringPolicy.scoringVersion()
            );
        }

        double weightSum = validEvidenceScores.stream()
            .mapToDouble(EvidenceScore::evidenceWeight)
            .sum();

        if (weightSum <= 0.0) {
            return ScoreCalculationResult.insufficient(
                evidenceScores.size(),
                evidenceScores,
                scoringPolicy.scoringVersion()
            );
        }

        double contributionSum = validEvidenceScores.stream()
            .mapToDouble(EvidenceScore::contribution)
            .sum();

        double normalizedScore = contributionSum / weightSum;
        normalizedScore = clamp(normalizedScore, -1.0, 1.0);

        int finalScore = toFinalScore(normalizedScore);
        Verdict verdict = scoringPolicy.determineVerdict(finalScore);

        return new ScoreCalculationResult(
            finalScore,
            verdict,
            round(normalizedScore),
            evidenceScores.size(),
            validEvidenceScores.size(),
            evidenceScores,
            scoringPolicy.scoringVersion()
        );
    }

    private EvidenceScore calculateEvidenceScore(EvidenceJudgment evidence) {
        Stance stance = evidence.stance();

        double relevanceScore = evidence.relevanceScore();
        double similarityScore = evidence.similarityScore();

        double evidenceWeight = relevanceScore * similarityScore;
        double stanceWeight = scoringPolicy.stanceWeight(stance);
        double contribution = evidenceWeight * stanceWeight;

        boolean valid = scoringPolicy.isScorableEvidence(
            stance,
            relevanceScore,
            similarityScore
        );

        return new EvidenceScore(
            evidence.evidenceId(),
            stance,
            round(relevanceScore),
            round(similarityScore),
            round(evidenceWeight),
            round(stanceWeight),
            round(contribution),
            valid
        );
    }

    private void validateEvidences(List<EvidenceJudgment> evidences) {
        for (EvidenceJudgment evidence : evidences) {
            if (evidence == null) {
                throw new IllegalArgumentException("EvidenceJudgment must not be null.");
            }

            if (evidence.stance() == null) {
                throw new IllegalArgumentException("Stance must not be null.");
            }

            validateScoreRange("relevanceScore", evidence.relevanceScore());
            validateScoreRange("similarityScore", evidence.similarityScore());
        }
    }

    private void validateScoreRange(String fieldName, double score) {
        if (Double.isNaN(score) || Double.isInfinite(score)) {
            throw new IllegalArgumentException(fieldName + " must be a valid number.");
        }

        if (score < 0.0 || score > 1.0) {
            throw new IllegalArgumentException(
                fieldName + " must be between 0.0 and 1.0. value=" + score
            );
        }
    }

    private int toFinalScore(double normalizedScore) {
        double rawScore = ((normalizedScore + 1.0) / 2.0) * 100.0;
        return (int) Math.round(clamp(rawScore, 0.0, 100.0));
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private double round(double value) {
        return Math.round(value * 10_000.0) / 10_000.0;
    }
}
