package com.sls.api.cognito.challenges.bean;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class VerifyAuthRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private Map<String, String> userAttributes;
  private Map<String, String> privateChallengeParameters;
  private String challengeAnswer;

}
