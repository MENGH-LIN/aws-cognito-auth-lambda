package com.sls.api.authentication.action;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.ForgotUserPasswordRequest;

public class ForgotPasswordAction extends AbstractAction {
  private AWSCognitoIdentityProvider cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  private String appClientId = ProviderFactory.getAppClientId();

  public String handle(String request, Context lambdaContext) throws BadRequestException {

    ForgotUserPasswordRequest input = getGson().fromJson(request, ForgotUserPasswordRequest.class);

    input.validate();

    ForgotPasswordResult forgotPasswordResult = null;
    try {
      forgotPasswordResult = cognitoUserPool
          .forgotPassword(new ForgotPasswordRequest().withUsername(input.getUsername()).withClientId(appClientId));
    } catch (Exception e) {
      throw new RuntimeException(getGson().toJson(e));
    }
    return getGson().toJson(forgotPasswordResult);
  }

}
