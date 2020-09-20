package com.sls.api.authentication.action;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.ConfirmUserForgotPasswordRequest;

public class ConfirmForgotPasswordAction extends AbstractAction {
  private AWSCredentialsProvider credentialsProvider;
  private AWSCognitoIdentityProvider cognitoUserPool;

  private String userPoolAppClientId;

  public ConfirmForgotPasswordAction() {
    this.credentialsProvider = ProviderFactory.getAWS_CREDENTIALS_PROVIDER();
    this.cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  }

  public String handle(String request, Context lambdaContext) throws BadRequestException {

    ConfirmUserForgotPasswordRequest input = getGson().fromJson(request, ConfirmUserForgotPasswordRequest.class);

    input.validate();

    ConfirmForgotPasswordResult confirmForgotPassword = null;

    try {
      confirmForgotPassword = cognitoUserPool
          .confirmForgotPassword(new ConfirmForgotPasswordRequest().withConfirmationCode(input.getConfirmationCode())
              .withPassword(input.getPassword()).withUsername(input.getUsername()).withClientId(userPoolAppClientId)
              .withRequestCredentialsProvider(credentialsProvider));
    } catch (Exception e) {
      throw new RuntimeException(getGson().toJson(e));
    }
    return getGson().toJson(confirmForgotPassword.getSdkHttpMetadata());
  }

}
