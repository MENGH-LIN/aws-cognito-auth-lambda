package com.sls.api.authentication.action;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.ChangePasswordRequest;
import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.ChangeUserPasswordRequest;

public class ChangePasswordAction extends AbstractAction {
  private AWSCognitoIdentityProvider cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  private AWSCredentialsProvider credentialsProvider = ProviderFactory.getAWS_CREDENTIALS_PROVIDER();

  public String handle(String request, Context lambdaContext) throws BadRequestException {
    ChangeUserPasswordRequest input = getGson().fromJson(request, ChangeUserPasswordRequest.class);

    input.validate();

    ChangePasswordResult changePassword = null;
    try {
      changePassword = cognitoUserPool.changePassword(new ChangePasswordRequest()
          .withAccessToken(input.getAccessToken()).withProposedPassword(input.getProposedPassword())
          .withPreviousPassword(input.getPreviousPassword()).withRequestCredentialsProvider(credentialsProvider));
    } catch (Exception e) {
      throw new RuntimeException(getGson().toJson(e));
    }
    return getGson().toJson(changePassword.getSdkHttpMetadata());
  }
}
