package com.yaho.factchecker.infrastructure.ai.prompt;

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
            throw new IllegalStateException("프롬프트 파일을 읽을 수 없습니다: " + path, e);
        }
    }
}
