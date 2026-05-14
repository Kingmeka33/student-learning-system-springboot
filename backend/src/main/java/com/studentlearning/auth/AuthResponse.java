package com.studentlearning.auth;

public record AuthResponse(String accessToken, UserInfo user) {
  public record UserInfo(String id, String name, String email, UserRole role) {}
}
