-- ============================================================
-- DB 최초 기동 시 1회 자동 실행 (docker-entrypoint-initdb.d)
-- pgvector extension 활성화
-- 데이터 볼륨이 비어있는 최초 기동에만 실행됩니다.
-- 기존 DB라면 수동 실행: CREATE EXTENSION IF NOT EXISTS vector;
-- ============================================================

CREATE EXTENSION IF NOT EXISTS vector;
