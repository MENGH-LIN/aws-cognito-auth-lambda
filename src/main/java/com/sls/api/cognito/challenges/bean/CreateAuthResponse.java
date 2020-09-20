package com.sls.api.cognito.challenges.bean;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class CreateAuthResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private String challengeMetadata;
  private Map<String, String> publicChallengeParameters;
  private Map<String, String> privateChallengeParameters;
}
