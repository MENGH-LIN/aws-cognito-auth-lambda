package com.sls.api.authentication.action;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminResetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.AdminResetUserPasswordResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.ResetPasswordRequest;

public class ResetPasswordAction extends AbstractAction {
  private AWSCredentialsProvider credentialsProvider = ProviderFactory.getAWS_CREDENTIALS_PROVIDER();
  private AWSCognitoIdentityProvider cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  private String userPoolId = ProviderFactory.getUserPoolId();

  public String handle(String request, Context lambdaContext) throws BadRequestException {

    ResetPasswordRequest input = getGson().fromJson(request, ResetPasswordRequest.class);

    input.validate();

    AdminResetUserPasswordResult adminResetUserPasswordResult = null;
    try {
      adminResetUserPasswordResult = cognitoUserPool
          .adminResetUserPassword(new AdminResetUserPasswordRequest().withUsername(input.getUsername())
              .withUserPoolId(userPoolId).withRequestCredentialsProvider(credentialsProvider));
    } catch (Exception e) {
      throw new RuntimeException(getGson().toJson(e));
    }
    return getGson().toJson(adminResetUserPasswordResult.getSdkHttpMetadata());
  }

}
