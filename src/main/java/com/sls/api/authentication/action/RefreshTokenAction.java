package com.sls.api.authentication.action;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.lambda.runtime.Context;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.RefreshTokenRequest;

public class RefreshTokenAction extends AbstractAction {
  private AWSCognitoIdentityProvider cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  private AWSCredentialsProvider credentialsProvider = ProviderFactory.getAWS_CREDENTIALS_PROVIDER();
  private String appClientId = ProviderFactory.getAppClientId();
  private String userPoolId = ProviderFactory.getUserPoolId();

  @Override
  public String handle(String request, Context lambdaContext) throws BadRequestException {

    RefreshTokenRequest input = getGson().fromJson(request, RefreshTokenRequest.class);

    input.validate();

    AdminInitiateAuthResult adminInitiateAuth = null;
    try {
      adminInitiateAuth = cognitoUserPool.adminInitiateAuth(new AdminInitiateAuthRequest()
          .withAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH).withClientId(appClientId).withUserPoolId(userPoolId)
          .withAuthParameters(input.getAuthParameters()).withRequestCredentialsProvider(credentialsProvider));
    } catch (Exception e) {
      throw new RuntimeException(getGson().toJson(e));
    }
    return getGson().toJson(adminInitiateAuth);
  }
}
