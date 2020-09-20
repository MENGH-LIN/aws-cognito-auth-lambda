package com.sls.api.authentication.exception;

/**
 * This exception is thrown whenever this service is not authorize to
 * communicate with another AWS service, it should not be exposed to Lambda or
 * returned to the client. When this exception is caught we should throw an
 * InternalErrorException
 */
public class AuthorizationException extends Exception {
  private static final long serialVersionUID = 1L;

  public AuthorizationException(String s, Exception e) {
    super(s, e);
  }

  public AuthorizationException(String s) {
    super(s);
  }
}
