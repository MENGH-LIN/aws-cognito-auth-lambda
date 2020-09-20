package com.sls.api.authentication.action;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.VerifyUserAttributeRequest;
import com.amazonaws.services.cognitoidp.model.VerifyUserAttributeResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.VerifyContactRequest;

public class ConfirmVerificationAction extends AbstractAction {
  private AWSCognitoIdentityProvider cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  private AWSCredentialsProvider credentialsProvider = ProviderFactory.getAWS_CREDENTIALS_PROVIDER();

  public String handle(String request, Context lambdaContext) throws BadRequestException {

    VerifyContactRequest input = getGson().fromJson(request, VerifyContactRequest.class);

    input.validate();

    VerifyUserAttributeResult verifyUserAttributeResult = null;
    try {
      verifyUserAttributeResult = cognitoUserPool.verifyUserAttribute(
          new VerifyUserAttributeRequest().withAccessToken(input.getAccessToken()).withCode(input.getConfirmationCode())
              .withAttributeName(input.getAttribute().name()).withRequestCredentialsProvider(credentialsProvider));
    } catch (Exception e) {
      throw new RuntimeException(getGson().toJson(e));
    }
    return getGson().toJson(verifyUserAttributeResult.getSdkHttpMetadata());
  }
}
