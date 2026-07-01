package com.yaho.factchecker.domain.ai.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record EvidenceForStanceRequest(
        UUID evidenceDocumentId, // 준기님이 관리하는 근거자료 ID
        String title, // 근거자료 제목
        String content, // LLM이 읽을 내용
        LocalDate publishedAt // 근거자료 발행일
) {
}
