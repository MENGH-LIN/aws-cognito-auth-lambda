package com.sls.api.authentication.model.action;

import static com.sls.api.authentication.helper.ValidationHelper.hasSpace;
import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;
import static com.sls.api.authentication.helper.ValidationHelper.isValidEmail;
import static com.sls.api.authentication.helper.ValidationHelper.isValidPhoneNumber;
import static com.sls.api.authentication.helper.ValidationHelper.isValidUsername;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.model.MessageMediumType;
import com.sls.api.authentication.model.UserGroupType;

import lombok.Data;

@Data
public class RegisterUserRequest {
  private String name;
  private String username;
  private String email;
  private String phoneNumber;
  private UserGroupType userGroup;
  private String company;
  private String publicARN;
  private String setPassword;

  public void validate() throws BadRequestException {
    if (isStringEmpty(name) || isStringEmpty(username) || (isStringEmpty(phoneNumber) && isStringEmpty(email)))
      throw new BadRequestException("Required Data is incomplete");

    if (userGroup == null) {
      throw new BadRequestException("User Group is required");
    }

    if (hasSpace(username))
      throw new BadRequestException("Space is not allowed in username");

    if (!isStringEmpty(phoneNumber))
      if (!isValidPhoneNumber(phoneNumber))
        throw new BadRequestException("Phone Number is invalid");

    if (!isStringEmpty(email))
      if (!isValidEmail(email))
        throw new BadRequestException("Email is invalid");

    if (!isValidUsername(username)) {
      throw new BadRequestException("Username is invalid - Username cannot have double underscores.");
    }
  }

  public ArrayList<String> setCommunicationMedium(RegisterUserRequest input) {
    ArrayList<String> mediums = new ArrayList<String>(2);
    if (!isStringEmpty(input.getPhoneNumber())) {
      mediums.add(MessageMediumType.SMS.name());
    }
    if (!isStringEmpty(input.getEmail())) {
      mediums.add(MessageMediumType.EMAIL.name());
    }

    return mediums;
  }

  public Collection<AttributeType> getAttributes() {
    List<AttributeType> list = new ArrayList<AttributeType>();
    list.add(new AttributeType().withName("name").withValue(name));
    list.add(new AttributeType().withName("custom:company").withValue(company));
    list.add(new AttributeType().withName("custom:publicARN").withValue(publicARN));

    if (!isStringEmpty(email)) {
      list.add(new AttributeType().withName("email").withValue(email.toLowerCase()));
    }

    if (!isStringEmpty(phoneNumber)) {
      list.add(new AttributeType().withName("phone_number").withValue(phoneNumber));
    }

    return list;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    if (email != null)
      return email.toLowerCase();
    return null;
  }
}
