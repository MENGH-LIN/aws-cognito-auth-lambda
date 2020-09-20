package com.sls.api.authentication.model.action;

import static com.sls.api.authentication.helper.ValidationHelper.hasSpace;
import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;

import java.util.HashMap;
import java.util.Map;

import com.sls.api.authentication.exception.BadRequestException;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
  private String deviceKey;

  public void validate() throws BadRequestException {
    if (isStringEmpty(username) || isStringEmpty(password))
      throw new BadRequestException("Username and Password are required.");

    if (hasSpace(username))
      throw new BadRequestException("Username cannot have space.");

    if (hasSpace(password))
      throw new BadRequestException("Password cannot have space.");
  }

  public Map<String, String> getAuthParameters() {
    Map<String, String> attributes = new HashMap<String, String>();
    attributes.put("USERNAME", getUsername());
    attributes.put("PASSWORD", getPassword());
    if (!isStringEmpty(deviceKey))
      attributes.put("DEVICE_KEY", deviceKey);
    return attributes;
  }

  public String getUsername() {
    return username;
  }
}
