package com.sls.api.authentication.model.action;

import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sls.api.authentication.exception.BadRequestException;

import lombok.Data;

@Data
public class RefreshTokenRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String refreshToken;
  private String deviceKey;

  public void validate() throws BadRequestException {
    if (isStringEmpty(refreshToken))
      throw new BadRequestException("Required Data is incomplete");
  }

  public Map<String, String> getAuthParameters() {
    Map<String, String> attributes = new HashMap<String, String>();
    attributes.put("REFRESH_TOKEN", refreshToken);
    if (!isStringEmpty(deviceKey))
      attributes.put("DEVICE_KEY", deviceKey);
    return attributes;
  }

}
