package com.yaho.factchecker.infrastructure.ai.adapter;

import com.yaho.factchecker.application.ai.port.ClaimAnalysisPort;
import com.yaho.factchecker.domain.ai.dto.request.ClaimAnalysisRequest;
import com.yaho.factchecker.domain.ai.dto.response.ClaimAnalysisResponse;
import org.springframework.stereotype.Component;

@Component
public class OpenAiClaimAnalysisAdapter implements ClaimAnalysisPort {

    @Override
    public ClaimAnalysisResponse analyze(ClaimAnalysisRequest request) {
        return null;
    }

}
