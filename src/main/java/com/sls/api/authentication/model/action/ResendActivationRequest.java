package com.sls.api.authentication.model.action;

import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;

import com.sls.api.authentication.exception.BadRequestException;

public class ResendActivationRequest {

  private String username;

  public void validate() throws BadRequestException {
    if (isStringEmpty(username))
      throw new BadRequestException("Required Data is incomplete");
  }

  public String getUsername() {
    return username;
  }

}