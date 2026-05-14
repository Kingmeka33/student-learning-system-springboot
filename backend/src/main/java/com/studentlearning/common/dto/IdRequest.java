package com.studentlearning.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/** DTO used for endpoints that receive an ID through the URL path. */
public record IdRequest(
    @Schema(example = "uuid-value") @NotBlank String id
) {}
