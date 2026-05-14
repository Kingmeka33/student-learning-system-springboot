package com.studentlearning.students;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class StudentDtos {
  public record CreateStudentRequest(
      @Schema(example = "Emeka Oramalu") @NotBlank String name,
      @Schema(example = "emeka@test.com") @Email String email
  ) {}

  public record UpdateStudentRequest(
      @Schema(example = "Emeka Updated") String name,
      @Schema(example = "updated@test.com") @Email String email
  ) {}
}
