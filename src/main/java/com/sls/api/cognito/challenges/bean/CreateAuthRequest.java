package com.sls.api.cognito.challenges.bean;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class CreateAuthRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private Map<String, String> userAttributes;
  private ChallengeResult[] session;
  private String challengeName;

}
