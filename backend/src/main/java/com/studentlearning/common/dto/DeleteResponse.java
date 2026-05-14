package com.studentlearning.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** DTO returned after delete/soft-delete actions. */
public record DeleteResponse(
    @Schema(example = "StudentEntity deleted successfully") String message
) {}
