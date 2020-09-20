package com.sls.api.cognito.challenges.bean;

import java.io.Serializable;

import com.sls.api.cognito.challenges.utils.CallerContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyAuthChallenge implements Serializable {

  private static final long serialVersionUID = 1L;

  String version;
  String region;
  String userPoolId;
  String userName;
  CallerContext callerContext;
  String triggerSource;
  VerifyAuthRequest request;
  VerifyAuthResponse response;

}
