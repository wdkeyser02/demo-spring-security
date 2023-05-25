package com.demo.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * @author : Demo User
 * @date : 13-Apr-2023
 */
public class ConflictException extends BaseException {

  /**
   *
   */
  private static final long serialVersionUID = 2206386282229373207L;
  private static final HttpStatus status = HttpStatus.CONFLICT;

  /**
   *
   */
  public ConflictException() {}

  /**
   * @param status
   * @param message
   * @param cause
   */
  public ConflictException(final String message, final Throwable cause) {
    super(status, message, cause);
  }

  /**
   * @param status
   * @param message
   */
  public ConflictException(final String message) {
    super(status, message);
  }

  /**
   * @param status
   * @param cause
   */
  public ConflictException(final Throwable cause) {
    super(status, cause);
  }

}
