package com.salesmanager.shop.store.api.exception;

public class UserAlreadyExistRuntimeException extends GenericRuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public UserAlreadyExistRuntimeException(String errorCode, String message) {
    super(errorCode, message);
  }

  public UserAlreadyExistRuntimeException(String message) {
    super(message);
  }

  public UserAlreadyExistRuntimeException(Throwable exception) {
    super(exception);
  }

  public UserAlreadyExistRuntimeException(String message, Throwable exception) {
    super(message, exception);
  }

  public UserAlreadyExistRuntimeException(String errorCode, String message, Throwable exception) {
    super(errorCode, message, exception);
  }
}
