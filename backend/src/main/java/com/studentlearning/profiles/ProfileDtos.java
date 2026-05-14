package com.studentlearning.profiles;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class ProfileDtos {
  public record CreateProfileRequest(
      @Schema(example = "Hardworking IT student") @NotBlank String bio,
      @Schema(example = "https://api.dicebear.com/7.x/avataaars/svg?seed=student1") String avatarUrl,
      @Schema(example = "student-uuid") @NotBlank String studentId
  ) {}
  public record UpdateProfileRequest(String bio, String avatarUrl, String studentId) {}
}
