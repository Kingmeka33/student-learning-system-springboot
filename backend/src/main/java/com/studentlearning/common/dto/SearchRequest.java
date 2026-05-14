package com.studentlearning.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** DTO used for list/search endpoints. */
public record SearchRequest(
    @Schema(example = "math", nullable = true) String search
) {}
