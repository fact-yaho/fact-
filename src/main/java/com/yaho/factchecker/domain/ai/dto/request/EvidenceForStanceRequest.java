package com.yaho.factchecker.domain.ai.dto.request;

import java.time.LocalDate;

public record EvidenceForStanceRequest(
        String sourceDocumentId, // 준기님이 관리하는 근거자료 ID
        String title, // 근거자료 제목
        String content, // LLM이 읽을 내용
        LocalDate publishedAt, // 근거자료 발행일
        Integer retrievalRank // 준기님이 계산한 근거자료 관련도 우선 순위
) {
}
