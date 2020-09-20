package com.sls.api.authentication.exception;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
 * This exception should be thrown whenever requests fail validation. The
 * exception sets a default pattern in the string "BAD_REQ: .*" that can be
 * easily matched from the API Gateway for mapping.
 */
public class BadRequestException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public BadRequestException(String s, Exception e) {
    super(s, e);
  }

  @Override
  public String getMessage() {
    Map<String, Object> errorPayload = new HashMap<String, Object>();
    errorPayload.put("errorType", "Client");
    errorPayload.put("errorCode", "BadRequestException");
    errorPayload.put("statusCode", 400);
    errorPayload.put("serviceName", "WealthdeskAuth");
    errorPayload.put("errorMessage", super.getMessage());
    String message = new Gson().toJson(errorPayload);
    return message;
  }

  public BadRequestException(String s) {
    super(s);
  }
}
