package com.studentlearning.common.security;

import com.studentlearning.auth.UserRole;

/**
 * Central place for role string values.
 *
 * Controllers still use Spring Security functions like hasRole(...),
 * but the actual role words ADMIN/STUDENT are stored here so they are easy to edit.
 */
public final class SecurityRoleNames {
  private SecurityRoleNames() {}

  public static final String ADMIN_ROLE = "ADMIN";
  public static final String STUDENT_ROLE = "STUDENT";
  public static final String ROLE_PREFIX = "ROLE_";

  public static String from(UserRole role) {
    return role.name();
  }
}
