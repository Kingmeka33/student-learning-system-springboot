package com.studentlearning.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
    @Schema(example = "Admin User") @NotBlank String name,
    @Schema(example = "admin@test.com") @Email String email,
    @Schema(example = "password123") @NotBlank String password,
    @Schema(example = "ADMIN") @NotNull UserRole role
) {}
