package com.studentlearning.assignments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AssignmentDtos {
  public record CreateAssignmentRequest(
      @Schema(example = "Algebra Homework") @NotBlank String title,
      @Schema(example = "2026-06-01") @NotNull LocalDate dueDate,
      @Schema(example = "course-uuid") @NotBlank String courseId
  ) {}
  public record UpdateAssignmentRequest(String title, LocalDate dueDate, String courseId) {}
}
