package com.sls.api.authentication.model.action;

import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;

import java.io.Serializable;

import com.sls.api.authentication.exception.BadRequestException;

import lombok.Getter;

@Getter
public class ResetPasswordRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String username;

  public void validate() throws BadRequestException {
    if (isStringEmpty(username))
      throw new BadRequestException("Username is empty");
  }

  public String getUsername() {
    return username;
  }

}
