package com.yaho.factchecker.domain.retrieval.service;

import com.yaho.factchecker.domain.retrieval.dto.VectorResult;
import com.yaho.factchecker.domain.retrieval.repository.DocumentFactRepository;
import com.yaho.factchecker.domain.retrieval.repository.projection.DocumentVectorScoreProjection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * score() 역할 : 특정 소주장에 대해 후보 문서들의 벡터 유사도 점수/순위를 구하는 오케스트레이터
 *
 * claimId = 대상 소주장 id
 * queryVector = pgvector 리터럴 문자열 "[v1,v2,...]"
 * 최종 반환결과 =  문서별 벡터 결과 (유사도 내림차순, rank 1부터)
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class VectorScorer {

    private final DocumentFactRepository documentFactRepository;

    // repository에서 문서별 유사도를 받고, 내림차순으로 정렬해 순위를 매겨 VectorResult 형태로 반환
    public List<VectorResult> score(java.util.UUID claimId, String queryVector)
    {
        if (claimId == null || queryVector == null || queryVector.isBlank()) {
            return java.util.Collections.emptyList();
        }

        // Repository에서 각 소주장이 수집한 문서의 모든 fact들에 대해 코사인 비교
        // 각 문서별로 MAX 유사도로 집계하고, 최종 (문서id, maxSimilarity)형태로 반환
        List<DocumentVectorScoreProjection> projections =
                documentFactRepository.findDocumentVectorScores(claimId, queryVector);

        // 가져온 (id - maxSmiliarity)에서 유사도 내림차순으로 정렬
        List<DocumentVectorScoreProjection> sorted = new ArrayList<>(projections);
        sorted.sort(Comparator.comparingDouble(
                DocumentVectorScoreProjection::getMaxSimilarity).reversed());

        // 순위를 추가하여 VectorResult(id, 유사도, 순위) 레코드 형태로 반환
        List<VectorResult> results = new ArrayList<>(sorted.size());
        for (int i = 0; i < sorted.size(); i++) {
            DocumentVectorScoreProjection p = sorted.get(i);
            results.add(new VectorResult(
                    p.getEvidenceDocumentId(),
                    p.getMaxSimilarity(),
                    i + 1
            ));
        }
        return results;
    }
}
