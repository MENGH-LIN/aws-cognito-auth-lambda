package com.sls.api.cognito.challenges.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class DefineAuthResponse implements Serializable {
  private static final long serialVersionUID = 1L;
  private String challengeName;
  private boolean issueTokens;
  private boolean failAuthentication;

}
