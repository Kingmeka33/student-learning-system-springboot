package com.studentlearning.common.exception;

import org.springframework.http.HttpStatus;

// Simple custom exception: only returns the message, making debugging easier.
public class ApiException extends RuntimeException {
  private final HttpStatus status;

  public ApiException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() { return status; }
}
