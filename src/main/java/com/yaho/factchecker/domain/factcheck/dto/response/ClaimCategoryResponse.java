package com.yaho.factchecker.domain.factcheck.dto.response;

import com.yaho.factchecker.global.type.ClaimCategory;
import java.util.UUID;

public record ClaimCategoryResponse(
        UUID categoryId,
        ClaimCategory categoryName,
        boolean primaryCategory
) {
}
