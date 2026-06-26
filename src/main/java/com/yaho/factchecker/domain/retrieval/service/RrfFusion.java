package com.yaho.factchecker.domain.retrieval.service;

import com.yaho.factchecker.domain.retrieval.dto.Bm25Result;
import com.yaho.factchecker.domain.retrieval.dto.RerankRow;
import com.yaho.factchecker.domain.retrieval.dto.VectorResult;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RrfFusion {

    private static final int DEFAULT_K = 60;

    public List<RerankRow> fuse(List<Bm25Result> bm25Results, List<VectorResult> vectorResults) {
        return fuse(bm25Results, vectorResults, DEFAULT_K);
    }

    // 추후 K 값을 조정할 때 사용하는 오버로드된 메서드
    public List<RerankRow> fuse(List<Bm25Result> bm25Results,
                                List<VectorResult> vectorResults,
                                int k)
    {
        // 빠른 탐색을 위해 BM25의 List를 Map으로 변환
        Map<UUID, Bm25Result> bm25Map = new HashMap<>();
        for (Bm25Result b : bm25Results) bm25Map.put(b.evidenceDocumentId(), b);

        // 빠른 탐색을 위해 VectorResult의 List를 Map으로 변환
        Map<UUID, VectorResult> vectorMap = new HashMap<>();
        for (VectorResult v : vectorResults) vectorMap.put(v.evidenceDocumentId(), v);

        // BM25와 Vector에서 처리한 문서들을 중복 없이 Set으로 모음
        Set<UUID> allDocIds = new HashSet<>();
        allDocIds.addAll(bm25Map.keySet());
        allDocIds.addAll(vectorMap.keySet());

        // 각 문서들에 대해 최종 RRF를 계산한 결과를 Scored 형태로 저장
        List<Scored> scoredList = new ArrayList<>();

        // 각 문서별 RRF 점수 계산
        for (UUID docId : allDocIds) {
            // 각 문서별로 BM25 결과와 Vecotr 결과를 가져옴
            Bm25Result bm25 = bm25Map.get(docId);
            VectorResult vector = vectorMap.get(docId);

            /*
            * RRF_score = 1/(k + bm25_rank) + 1/(k + vector_rank)
            * 상단 공식의 bm25와 vector에 해당하는 부분에 대한 계산을 수행
            * */
            double bm25Term = (bm25 != null) ? 1.0 / (k + bm25.rank()) : 0.0;
            double vectorTerm = (vector != null) ? 1.0 / (k + vector.rank()) : 0.0;

            // bm25와 vector에 대해 각각 점수와 순위가 없다면 0으로 저장하고,
            // 최종적으로 RRF_socre의 값을 Scored 형태에 저장
            scoredList.add(new Scored(
                    docId,
                    (bm25 != null) ? bm25.score() : 0.0,
                    (bm25 != null) ? bm25.rank() : 0,
                    (vector != null) ? vector.score() : 0.0,
                    (vector != null) ? vector.rank() : 0,
                    bm25Term + vectorTerm
            ));
        }

        // RRF 점수를 기준으로 내림차순 정렬
        scoredList.sort(Comparator.comparingDouble(Scored::rrfScore).reversed());

        // final_rank(1부터) 부여하여 RerankRow 생성하고,
        // 최종 RerankRow 형태로 된 List를 전달
        List<RerankRow> ranked = new ArrayList<>(scoredList.size());
        for (int i = 0; i < scoredList.size(); i++) {
            Scored s = scoredList.get(i);
            ranked.add(new RerankRow(
                    s.docId(), s.bm25Score(), s.bm25Rank(),
                    s.vectorScore(), s.vectorRank(), i + 1
            ));
        }
        return ranked;
    }

    // 정렬 전용 내부 자료형
    private record Scored(
            UUID docId,
            double bm25Score, int bm25Rank,
            double vectorScore, int vectorRank,
            double rrfScore
    ) {}
}
