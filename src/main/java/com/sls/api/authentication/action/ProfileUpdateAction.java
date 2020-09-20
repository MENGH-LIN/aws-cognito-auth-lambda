package com.sls.api.authentication.action;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.lambda.runtime.Context;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.UserUpdateRequest;

public class ProfileUpdateAction extends AbstractAction {
  private AWSCognitoIdentityProvider cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  private AWSCredentialsProvider credentialsProvider = ProviderFactory.getAWS_CREDENTIALS_PROVIDER();
  private String userPoolId = ProviderFactory.getUserPoolId();

  public String handle(String request, Context lambdaContext) throws BadRequestException {

    UserUpdateRequest input = getGson().fromJson(request, UserUpdateRequest.class);

    input.validate();

    AdminUpdateUserAttributesResult adminUpdateUserAttributesResult = null;
    try {
      adminUpdateUserAttributesResult = cognitoUserPool
          .adminUpdateUserAttributes(new AdminUpdateUserAttributesRequest().withUsername(input.getUsername())
              .withUserAttributes(
                  new AttributeType().withName(input.getAttributeName()).withValue(input.getAttributeValue()))
              .withUserPoolId(userPoolId).withRequestCredentialsProvider(credentialsProvider));
    } catch (Exception e) {
      throw new RuntimeException(getGson().toJson(e));
    }

    return getGson().toJson(adminUpdateUserAttributesResult.getSdkHttpMetadata());
  }
}
