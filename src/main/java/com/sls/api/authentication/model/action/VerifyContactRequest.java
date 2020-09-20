package com.sls.api.authentication.model.action;

import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;

import java.io.Serializable;

import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.model.VerifyingAttributes;

import lombok.Data;

@Data
public class VerifyContactRequest implements Serializable {
  private static final long serialVersionUID = 1L;
  private VerifyingAttributes attribute;
  private String accessToken;
  private String confirmationCode;

  public void validate() throws BadRequestException {
    if (attribute == null)
      throw new BadRequestException("Which attribute to verify is not mentioned");
    if (isStringEmpty(accessToken))
      throw new BadRequestException("Access token cannot be blank");
    if (isStringEmpty(confirmationCode))
      throw new BadRequestException("confirmation code cannot be blank");
  }

}
