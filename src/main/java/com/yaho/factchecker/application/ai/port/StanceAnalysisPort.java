package com.yaho.factchecker.application.ai.port;

import com.yaho.factchecker.domain.ai.dto.request.StanceAnalysisRequest;
import com.yaho.factchecker.domain.ai.dto.response.StanceAnalysisResponse;

public interface StanceAnalysisPort {

    StanceAnalysisResponse analyze(StanceAnalysisRequest request);

}
