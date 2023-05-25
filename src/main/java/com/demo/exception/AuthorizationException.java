package com.demo.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * @author : Demo User
 * @date : 13-Apr-2023
 */
public class AuthorizationException extends BaseException {

  /**
   *
   */
  private static final long serialVersionUID = 8569404917204068340L;
  private static final HttpStatus status = HttpStatus.UNAUTHORIZED;

  /**
   *
   */
  public AuthorizationException() {}

  /**
   * @param status
   * @param message
   * @param cause
   */
  public AuthorizationException(final String message, final Throwable cause) {
    super(status, message, cause);
  }

  /**
   * @param status
   * @param message
   */
  public AuthorizationException(final String message) {
    super(status, message);
  }

  /**
   * @param status
   * @param cause
   */
  public AuthorizationException(final Throwable cause) {
    super(status, cause);
  }

}
