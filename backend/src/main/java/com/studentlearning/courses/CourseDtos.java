package com.studentlearning.courses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CourseDtos {
  public record CreateCourseRequest(
      @Schema(example = "Mathematics") @NotBlank String title,
      @Schema(example = "MATH101") @NotBlank String code
  ) {}
  public record UpdateCourseRequest(String title, String code) {}
}
