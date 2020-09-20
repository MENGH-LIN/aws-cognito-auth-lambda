package com.sls.api.cognito.challenges.bean;

import java.io.Serializable;

import com.sls.api.cognito.challenges.utils.CallerContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefineAuthChallenge implements Serializable {

  private static final long serialVersionUID = 1L;

  private String version;
  private String region;
  private String userPoolId;
  private String userName;
  private CallerContext callerContext;
  private String triggerSource;
  private DefineAuthRequest request;
  private DefineAuthResponse response;

}
