package com.sls.api.cognito.challenges.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class ChallengeResult implements Serializable {

  private static final long serialVersionUID = 1L;

  private String challengeName;
  private boolean challengeResult;
  private String challengeMetadata;

}
