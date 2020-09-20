package com.sls.api.cognito.challenges.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class VerifyAuthResponse implements Serializable {
  private static final long serialVersionUID = 1L;
  private boolean answerCorrect;
}
