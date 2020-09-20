package com.sls.api.authentication.exception;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class RuntimeServerException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public RuntimeServerException(String s, Exception e) {
    super(s, e);
  }

  @Override
  public String getMessage() {
    Map<String, Object> errorPayload = new HashMap<String, Object>();
    errorPayload.put("errorType", "Server");
    errorPayload.put("errorCode", "RuntimeServerException");
    errorPayload.put("statusCode", 500);
    errorPayload.put("serviceName", "WealthdeskAuth");
    errorPayload.put("errorStack", super.getStackTrace());
    errorPayload.put("errorMessage", super.getMessage());
    return new Gson().toJson(errorPayload);
  }

  public RuntimeServerException(String s) {
    super(s);
  }

}
