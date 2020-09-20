package com.sls.api.authentication.model.action;

import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;

import java.io.Serializable;

import com.sls.api.authentication.exception.BadRequestException;

import lombok.Data;

@Data
public class ChangeUserPasswordRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String accessToken;
  private String previousPassword;
  private String proposedPassword;

  public void validate() throws BadRequestException {
    if (isStringEmpty(accessToken)) {
      throw new BadRequestException("Access Token needed.");
    }
    if (isStringEmpty(proposedPassword)) {
      throw new BadRequestException("New password is needed.");
    }
    if (isStringEmpty(previousPassword)) {
      throw new BadRequestException("Previous Password is needed.");
    }
  }
}
