package com.yaho.factchecker.domain.ai.entity;

import com.yaho.factchecker.domain.ai.type.AiCallStatus;
import com.yaho.factchecker.domain.ai.type.AiCallType;
import com.yaho.factchecker.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Table(name = "ai_call_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiCallLog extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "ai_call_log_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "call_type", length = 30, nullable = false)
    private AiCallType callType;

    @Column(name = "model_name", length = 100, nullable = false)
    private String modelName;

    @Column(name = "prompt_path", length = 255, nullable = false)
    private String promptPath;

    @Column(name = "prompt_version", length = 20)
    private String promptVersion;

    @Column(name = "input_data", columnDefinition = "TEXT", nullable = false)
    private String inputData;

    @Column(name = "output_data", columnDefinition = "TEXT")
    private String outputData;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private AiCallStatus status;

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "latency_ms")
    private Long latencyMs;

    @Builder
    private AiCallLog(
            AiCallType callType,
            String modelName,
            String promptPath,
            String promptVersion,
            String inputData,
            String outputData,
            AiCallStatus status,
            String errorCode,
            String errorMessage,
            Long latencyMs
    ) {
        this.callType = callType;
        this.modelName = modelName;
        this.promptPath = promptPath;
        this.promptVersion = promptVersion;
        this.inputData = inputData;
        this.outputData = outputData;
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.latencyMs = latencyMs;
    }
}
