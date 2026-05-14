package com.studentlearning.enrollments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class EnrollmentDtos {
  public record EnrollRequest(
      @Schema(example = "student-uuid") @NotBlank String studentId,
      @Schema(example = "course-uuid") @NotBlank String courseId
  ) {}

  public record MultiEnrollRequest(
      @Schema(example = "student-uuid", nullable = true) String studentId,
      @Schema(example = "[\"course-uuid-1\", \"course-uuid-2\"]") @NotEmpty List<String> courseIds
  ) {}

  public record EnrollmentSearchRequest(
      @Schema(example = "emeka", nullable = true) String search
  ) {}
}
