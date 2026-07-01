package com.yaho.factchecker.infrastructure.ai.prompt;

import com.yaho.factchecker.global.exception.BusinessException;
import com.yaho.factchecker.global.exception.ErrorCode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PromptLoader {

    private final ResourceLoader resourceLoader;

    public String load(String path) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + path);
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.AI_PROMPT_LOAD_FAILED, e);
        }
    }
}
