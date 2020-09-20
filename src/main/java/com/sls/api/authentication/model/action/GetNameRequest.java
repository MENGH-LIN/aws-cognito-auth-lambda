package com.sls.api.authentication.model.action;

import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.helper.ValidationHelper;

import lombok.Getter;

@Getter
public class GetNameRequest {
  private String username;

  public void validate() throws BadRequestException {
    if (ValidationHelper.isStringEmpty(username))
      throw new BadRequestException("username is required");
  }

  public String getUsername() {
    return username;
  }
}
