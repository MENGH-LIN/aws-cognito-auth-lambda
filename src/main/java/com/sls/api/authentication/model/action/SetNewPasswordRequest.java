package com.sls.api.authentication.model.action;

import static com.sls.api.authentication.helper.ValidationHelper.hasSpace;
import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.sls.api.authentication.exception.BadRequestException;

import lombok.Data;

@Data
public class SetNewPasswordRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String newPassword;
  private String username;
  private String session;

  public void validate() throws BadRequestException {
    if (isStringEmpty(newPassword) || isStringEmpty(username) || isStringEmpty(session))
      throw new BadRequestException("Required Data is incomplete");

    if (hasSpace(newPassword))
      throw new BadRequestException("Password cannot have space.");
  }

  public Map<String, String> getChallengeParameters() {
    Map<String, String> attributes = new HashMap<>();
    attributes.put("NEW_PASSWORD", getNewPassword());
    attributes.put("USERNAME", getUsername());
    return attributes;
  }

  public String getUsername() {
    return username;
  }

  public List<AttributeType> getAttributeToVerify(AdminGetUserResult userResult) {
    List<AttributeType> markVerify = new ArrayList<AttributeType>(2);
    userResult.getUserAttributes().stream()
        .filter(attribute -> attribute.getName().equals("phone_number") || attribute.getName().equals("email"))
        .forEach(attr -> {
          if (attr.getName().equals("email")) {
            markVerify.add(new AttributeType().withName("email_verified").withValue("true"));
          } else if (attr.getName().equals("phone_number")) {
            markVerify.add(new AttributeType().withName("phone_number_verified").withValue("true"));
          }
        });

    return markVerify;
  }
}
