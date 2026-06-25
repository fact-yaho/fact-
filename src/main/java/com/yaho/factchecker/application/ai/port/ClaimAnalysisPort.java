package com.yaho.factchecker.application.ai.port;

import com.yaho.factchecker.domain.ai.dto.request.ClaimAnalysisRequest;
import com.yaho.factchecker.domain.ai.dto.response.ClaimAnalysisResponse;

public interface ClaimAnalysisPort {

    ClaimAnalysisResponse analyze(ClaimAnalysisRequest request);

}
