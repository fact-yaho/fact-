-- ============================================================
-- HNSW 벡터 인덱스 (코사인 거리 기준)
-- 실행 시점: 앱을 한 번 실행해 Hibernate(ddl-auto=update)가
--           테이블을 생성한 "이후"에 수동으로 실행해야 함.
--
-- ▶ 실행 방법
--   [도커 DB + macOS/Linux]
--     docker exec -i factchecker-postgres \
--       psql -U user -d factchecker < db/hnsw/hnsw-index.sql
--
--   [도커 DB + Windows PowerShell]
--     Get-Content db/hnsw/hnsw-index.sql | `
--       docker exec -i factchecker-postgres psql -U user -d factchecker
--
--   [OS 무관 / 안전한 방식]
--     docker cp db/hnsw/hnsw-index.sql factchecker-postgres:/tmp/hnsw-index.sql
--     docker exec -it factchecker-postgres \
--       psql -U user -d factchecker -f /tmp/hnsw-index.sql
--
--   ※ -U(유저), -d(DB명)는 .env 의 POSTGRES_USER / POSTGRES_DB 와 일치시킬 것
--
-- 쿼리에서 반드시 <=> (코사인) 연산자를 써야 인덱스 적용.
-- IF NOT EXISTS 라 여러 번 실행해도 안전.
-- ============================================================

-- 1) 근거 검색용: 세부 사실(fact) 벡터
CREATE INDEX IF NOT EXISTS idx_document_fact_vector
    ON document_fact
    USING hnsw (fact_vector vector_cosine_ops);

-- 2) 유사질문(중복) 검사용: 소주장 임베딩 벡터
CREATE INDEX IF NOT EXISTS idx_claim_embedding_vector
    ON claim_embedding
    USING hnsw (claim_vector vector_cosine_ops);
