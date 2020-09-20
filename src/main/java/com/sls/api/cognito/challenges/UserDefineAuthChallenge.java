package com.sls.api.cognito.challenges;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sls.api.cognito.challenges.bean.DefineAuthChallenge;

public class UserDefineAuthChallenge implements RequestHandler<DefineAuthChallenge, DefineAuthChallenge> {

  @Override
  public DefineAuthChallenge handleRequest(DefineAuthChallenge input, Context context) {

    if (input.getRequest().getSession().length > 0 && input.getRequest().getSession()[0].isChallengeResult()) {
      input.getResponse().setIssueTokens(true);
      input.getResponse().setFailAuthentication(false);
    } else if (input.getRequest().getSession().length == 0) {
      input.getResponse().setIssueTokens(false);
      input.getResponse().setFailAuthentication(false);
      input.getResponse().setChallengeName("CUSTOM_CHALLENGE");
    } else {
      input.getResponse().setIssueTokens(false);
      input.getResponse().setFailAuthentication(true);
    }

    context.getLogger().log(input.toString());
    return input;
  }

}
