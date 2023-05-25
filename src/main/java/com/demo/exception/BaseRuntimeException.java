package com.demo.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * @author : Demo User
 * @date : 13-Apr-2023
 */
public class BaseRuntimeException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = -8453363075793355923L;
  /**
   *
   */
  private final HttpStatus status;

  /**
   *
   */
  public BaseRuntimeException() {
    status = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  /**
   * @param message
   * @param cause
   */
  public BaseRuntimeException(final HttpStatus status, final String message,
      final Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  /**
   * @param message
   */
  public BaseRuntimeException(final HttpStatus status, final String message) {
    super(message);
    this.status = status;
  }

  /**
   * @param cause
   */
  public BaseRuntimeException(final HttpStatus status, final Throwable cause) {
    super(cause);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
