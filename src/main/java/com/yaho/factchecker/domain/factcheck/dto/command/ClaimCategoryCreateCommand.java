package com.yaho.factchecker.domain.factcheck.dto.command;

import com.yaho.factchecker.global.type.ClaimCategory;

public record ClaimCategoryCreateCommand(
        ClaimCategory category,
        boolean primaryCategory
) {
}
