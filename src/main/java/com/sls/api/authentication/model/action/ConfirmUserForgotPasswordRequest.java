package com.sls.api.authentication.model.action;

import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;

import java.io.Serializable;

import com.sls.api.authentication.exception.BadRequestException;

import lombok.Getter;

@Getter
public class ConfirmUserForgotPasswordRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String confirmationCode;
  private String password;
  private String username;

  public void validate() throws BadRequestException {
    if (isStringEmpty(confirmationCode))
      throw new BadRequestException("Confirmation Code needed.");
    if (isStringEmpty(username))
      throw new BadRequestException("Username is needed.");
    if (isStringEmpty(password))
      throw new BadRequestException("Password is needed.");
  }

  public String getUsername() {
    return username;
  }

}
