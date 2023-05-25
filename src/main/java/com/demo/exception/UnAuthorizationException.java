package com.demo.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * @author : Demo User
 * @date : 13-Apr-2023
 */
public class UnAuthorizationException extends BaseException {

  /**
   *
   */
  private static final long serialVersionUID = 8569404917204068340L;
  private static final HttpStatus status = HttpStatus.UNAUTHORIZED;

  /**
   *
   */
  public UnAuthorizationException() {}

  /**
   * @param status
   * @param message
   * @param cause
   */
  public UnAuthorizationException(final String message, final Throwable cause) {
    super(status, message, cause);
  }

  /**
   * @param status
   * @param message
   */
  public UnAuthorizationException(final String message) {
    super(status, message);
  }

  /**
   * @param status
   * @param cause
   */
  public UnAuthorizationException(final Throwable cause) {
    super(status, cause);
  }

}
