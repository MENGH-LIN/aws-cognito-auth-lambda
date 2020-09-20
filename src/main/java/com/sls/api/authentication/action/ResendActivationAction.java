package com.sls.api.authentication.action;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.MessageActionType;
import com.amazonaws.services.lambda.runtime.Context;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.ResendActivationRequest;

public class ResendActivationAction extends AbstractAction {
  private String userPoolId = ProviderFactory.getUserPoolId();
  private AWSCognitoIdentityProvider cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  private AWSCredentialsProvider credentialsProvider = ProviderFactory.getAWS_CREDENTIALS_PROVIDER();
  private AdminCreateUserResult resendActivationRequest;

  public String handle(String request, Context lambdaContext) throws BadRequestException {

    ResendActivationRequest input = getGson().fromJson(request, ResendActivationRequest.class);

    input.validate();

    AdminGetUserResult user = cognitoUserPool.adminGetUser(new AdminGetUserRequest().withUsername(input.getUsername())
        .withUserPoolId(userPoolId).withRequestCredentialsProvider(credentialsProvider));

    List<AttributeType> collectAttributes = user.getUserAttributes().stream()
        .filter(attribute -> attribute.getName().equals("email") || attribute.getName().equals("phone_number"))
        .collect(Collectors.toList());
    try {
      if (collectAttributes.size() == 0)
        throw new IllegalArgumentException("Could not find email or phone number to communicate with user");

      if (collectAttributes.size() == 2)
        resendActivationRequest = cognitoUserPool
            .adminCreateUser(new AdminCreateUserRequest().withUsername(input.getUsername()).withUserPoolId(userPoolId)
                .withDesiredDeliveryMediums(Arrays.asList("EMAIL", "SMS")).withMessageAction(MessageActionType.RESEND)
                .withRequestCredentialsProvider(credentialsProvider));
      else
        collectAttributes.forEach(attribute -> {
          if (attribute.getName().equals("phone_number"))
            resendActivationRequest = cognitoUserPool.adminCreateUser(new AdminCreateUserRequest()
                .withUsername(input.getUsername()).withUserPoolId(userPoolId).withDesiredDeliveryMediums("SMS")
                .withMessageAction(MessageActionType.RESEND).withRequestCredentialsProvider(credentialsProvider));
          else
            resendActivationRequest = cognitoUserPool.adminCreateUser(new AdminCreateUserRequest()
                .withUsername(input.getUsername()).withUserPoolId(userPoolId).withDesiredDeliveryMediums("EMAIL")
                .withMessageAction(MessageActionType.RESEND).withRequestCredentialsProvider(credentialsProvider));
        });
    } catch (Exception e) {
      throw new RuntimeException(getGson().toJson(e));
    }
    return getGson().toJson(resendActivationRequest.getSdkHttpMetadata());
  }
}
