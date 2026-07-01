package com.yaho.factchecker.domain.retrieval.service;

import com.yaho.factchecker.domain.retrieval.dto.Bm25Result;
import com.yaho.factchecker.domain.retrieval.entity.EvidenceDocument;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * score() 역할 : 후보 문서들에 대해 쿼리(소주장)로 BM25 점수/순위를 구하는 오케스트레이터
 *
 * query = 정제된 소주장 텍스트
 * documents = 후보 근거문서 리스트
 * 최종 반환결과 = 문서별 BM25 결과 (점수 내림차순, rank 1부터). 점수 0(매칭 안 됨)인 문서도 포함
 */

@Slf4j
@Component
public class Bm25Scorer {

    private static final String FIELD_ID = "evidence_document_id"; // 문서 ID
    private static final String FIELD_TEXT = "text"; // 문서의 (title + content) 내용

    // 전체 BM25 결과를 구하는 오케스트레이터
    public List<Bm25Result> score(String query, List<EvidenceDocument> documents)
    {
        if (documents == null || documents.isEmpty() || query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        // Nori 한국어 형태소 분석기인 "KoreanAnalyzer" 사용,
        // ByteBuffersDirectory는 메모리 상의 인덱스 저장소
        try (Analyzer analyzer = new KoreanAnalyzer();
             Directory directory = new ByteBuffersDirectory()) {

            // Nori를 이용하여 인덱스 구축
            indexDocuments(directory, analyzer, documents);

            // 구축된 인덱스로 소주장에 대해 각 문서별로 점수와 순위를 평가
            return search(directory, analyzer, query, documents);

        } catch (Exception e) {
            log.error("BM25 점수 계산 실패. query='{}', docCount={}", query, documents.size(), e);
            return Collections.emptyList();
        }
    }



    // 각 문서들에 대한 인메모리 인덱스 구축 ("title + content_cleaned"에 대한 색인)
    private void indexDocuments(Directory directory, Analyzer analyzer,
                                List<EvidenceDocument> documents) throws Exception
    {
        // 아래의 IndexWriter가 어떤 언어(=한글) 분석기로 인덱스에 적을지 설정
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // IndexWriter = 인덱스에 문서의 색인을 써넣는 도구
        try (IndexWriter writer = new IndexWriter(directory, config)) {
            for (EvidenceDocument doc : documents) {
                // title + content_cleaned를 한 문장으로 합침
                String combinedText = buildText(doc);
                // Lucene 전용 객체 (Lucene에서 제공하는 Class)
                Document luceneDoc = new Document();
                // StringField는 분석을 하지 않는 필드를 지정함
                // evidence_document_id는 분석하지 않도록 함, 다만 어느 문서인지 식별을 위해 id를 저장 해둠
                luceneDoc.add(new StringField(FIELD_ID, doc.getEvidenceDocumentId().toString(), Field.Store.YES));
                // TextField는 분석하는 분석하려는 필드를 지정함
                // combinedText(title + content)를 분석하도록 지정, 다만 색인만 필요함으로 원문을 저장하지는 않음
                luceneDoc.add(new TextField(FIELD_TEXT, combinedText, Field.Store.NO));
                // 최종적으로 "색인 <-> 문서 id (문서 원문X)"로 인덱스 완성
                writer.addDocument(luceneDoc);
            }
        }
    }


    // 소주장에 대해 구축한 인덱스(indexDocuments)로 점수/순위를 평가
    private List<Bm25Result> search(Directory directory, Analyzer analyzer,
                                    String query, List<EvidenceDocument> documents) throws Exception
    {
        // DirectoryReader = 구축한 인덱스를 읽는 도구
        try (DirectoryReader reader = DirectoryReader.open(directory)) {
            // IndexSearcher = 실제 검색을 수행하는 객체
            IndexSearcher searcher = new IndexSearcher(reader);
            // IndexSearcher가 점수를 BM25로 평가하도록 명시적으로 지정
            searcher.setSimilarity(new BM25Similarity());

            // QueryParser = 소주장을 Lucene가 인식할 수 있는 Query객체로 변환해주는 객체
            // 소주장을 FILED_TEXT(= combined_text)를 대상으로 한글 분석기로 형태소 분석을 수행하도록
            QueryParser parser = new QueryParser(FIELD_TEXT, analyzer);
            // 소주장에 Lucene의 쿼리 문법용 특수 문자(+, -, 등)가 있어도 일반 텍스트로 취급하도록 처리
            Query luceneQuery = parser.parse(QueryParser.escape(query));

            // TopDocs = 검색 결과, 소주장에 따른 매칭된 문서들과 각자의 점수가 담김
            // API로 전달받은 전체 문서에 대해 점수를 평가도록 문서 전체의 크기(=documents.size())로 설정하여 search를 수행
            TopDocs topDocs = searcher.search(luceneQuery, documents.size());

            // "문서 id - BM25점수" 쌍으로 결과를 반환, 단 매칭되지 않는 경우는 점수가 없음
            java.util.Map<UUID, Double> scoreMap = new java.util.HashMap<>();
            // ScoreDoc = Lucene에서 제공하는 문서 한 건에 대한 결과 한 건
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = searcher.storedFields().document(scoreDoc.doc);
                UUID id = UUID.fromString(document.get(FIELD_ID));
                scoreMap.put(id, (double) scoreDoc.score);
            }

            // 각 문서들에 대해서 만약 색인 매칭이 안되어 점수가 없다면 0점으로 점수를 부여
            List<Bm25Result> results = new ArrayList<>();
            for (EvidenceDocument doc : documents) {
                UUID id = doc.getEvidenceDocumentId();
                double score = scoreMap.getOrDefault(id, 0.0);
                results.add(new Bm25Result(id, score, 0));
            }

            // 최종 점수 내림차순으로 정렬 후 rank 부여
            results.sort((a, b) -> Double.compare(b.score(), a.score()));

            // 후보 배치 내 min-max 정규화용 최소/최대 (정렬됐으니 첫째=max, 마지막=min)
            double max = results.get(0).score();
            double min = results.get(results.size() - 1).score();

            List<Bm25Result> ranked = new ArrayList<>(results.size());
            for (int i = 0; i < results.size(); ++i) {
                Bm25Result r = results.get(i);
                // BM25 원점을 0~1로 정규화한 값을 score로 저장 (rank는 정렬 순서 그대로라 RRF에 영향 없음)
                double normalized = normalizeScore(r.score(), min, max);
                ranked.add(new Bm25Result(r.evidenceDocumentId(), normalized, i + 1));
            }
            return ranked;
        }
    }


    /* BM25 정규화
     *  max == 0 -> 전부 0.0 (아무 문서도 키워드 매칭 안 됨 = 관련도 0)
     *  max == min (>0) -> 전부 1.0 (모두 동일하게, 유의미하게 관련)
     *  그 외 -> (score - min) / (max - min)
     */
    private double normalizeScore(double score, double min, double max)
    {
        // max == 0 이면 전부 0점이므로 max == min 검사보다 먼저 처리
        if (max == 0.0) {
            return 0.0;
        }
        if (max == min) {
            return 1.0;
        }
        return (score - min) / (max - min);
    }


    // title과 content를 합쳐서 하나의 문장으로 생성
    private String buildText(EvidenceDocument doc)
    {
        String title = doc.getTitle() == null ? "" : doc.getTitle();
        String content = doc.getContentCleaned() == null ? "" : doc.getContentCleaned();
        return (title + " " + content).trim();
    }
}
