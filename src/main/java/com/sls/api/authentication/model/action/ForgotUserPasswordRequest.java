package com.sls.api.authentication.model.action;

import java.io.Serializable;

import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.helper.ValidationHelper;

public class ForgotUserPasswordRequest implements Serializable {

  private static final long serialVersionUID = 1L;
  private String username;

  public void validate() throws BadRequestException {
    if (ValidationHelper.isStringEmpty(username))
      throw new BadRequestException("Username is empty");
  }

  public String getUsername() {
    return username;
  }
}
