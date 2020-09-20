package com.sls.api.cognito.challenges.utils;

import lombok.Data;

@Data
public class CallerContext {

  String awsSdkVersion;
  String userPoolAppClientId;

}
