package com.sls.api.authentication.exception;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
 * This exception should be thrown whenever requests fail validation. The
 * exception sets a default pattern in the string "BAD_REQ: .*" that can be
 * easily matched from the API Gateway for mapping.
 */
public class IncorrectStateException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public IncorrectStateException(String s, Exception e) {
    super(s, e);
  }

  @Override
  public String getMessage() {
    Map<String, Object> errorPayload = new HashMap<String, Object>();
    errorPayload.put("errorType", "Client");
    errorPayload.put("errorCode", "IncorrectStateException");
    errorPayload.put("statusCode", 417);
    errorPayload.put("serviceName", "WealthdeskAuth");
    errorPayload.put("errorMessage", super.getMessage());
    String message = new Gson().toJson(errorPayload);
    return message;
  }

  public IncorrectStateException(String s) {
    super(s);
  }
}
