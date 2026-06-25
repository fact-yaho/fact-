package com.yaho.factchecker.infrastructure.ai.adapter;

import com.yaho.factchecker.application.ai.port.StanceAnalysisPort;
import com.yaho.factchecker.domain.ai.dto.request.StanceAnalysisRequest;
import com.yaho.factchecker.domain.ai.dto.response.StanceAnalysisResponse;
import org.springframework.stereotype.Component;

@Component
public class OpenAiStanceAnalysisAdapter implements StanceAnalysisPort {

    @Override
    public StanceAnalysisResponse analyze(StanceAnalysisRequest request) {
        return null;
    }

}
