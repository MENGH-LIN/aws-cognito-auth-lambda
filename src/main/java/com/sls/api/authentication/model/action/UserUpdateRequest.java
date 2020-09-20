package com.sls.api.authentication.model.action;

import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;

import java.io.Serializable;

import com.sls.api.authentication.exception.BadRequestException;

import lombok.Data;

@Data
public class UserUpdateRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String username;
  private String attributeValue;
  private String attributeName;

  public void validate() throws BadRequestException {
    if (isStringEmpty(username))
      throw new BadRequestException("username is required.");
    if (isStringEmpty(attributeName))
      throw new BadRequestException("Attribute Name is required.");
    if (attributeName.equalsIgnoreCase("username"))
      throw new BadRequestException("You cannot update your username.");
    if (isStringEmpty(attributeValue))
      throw new BadRequestException("Attribute Value is required.");

    if (attributeName.equalsIgnoreCase("BrokerName"))
      attributeName = "BrokerName1";
    if (attributeName.equalsIgnoreCase("clientCode"))
      attributeName = "clientCode1";
  }

  public String getUsername() {
    return username;
  }
}
