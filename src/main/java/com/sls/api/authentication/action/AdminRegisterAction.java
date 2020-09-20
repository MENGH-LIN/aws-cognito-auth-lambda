package com.sls.api.authentication.action;

import static com.sls.api.authentication.helper.ValidationHelper.isStringEmpty;

import java.util.ArrayList;
import java.util.Collection;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.RegisterUserRequest;

public class AdminRegisterAction extends AbstractAction {

  private AWSCognitoIdentityProvider cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  private AWSCredentialsProvider credentialsProvider = ProviderFactory.getAWS_CREDENTIALS_PROVIDER();

  private String userPoolId = ProviderFactory.getUserPoolId();

  private LambdaLogger logger;
  private AdminGetUserResult adminGetUser;

  public String handle(String request, Context lambdaContext) throws BadRequestException {
    logger = lambdaContext.getLogger();
    RegisterUserRequest input = getGson().fromJson(request, RegisterUserRequest.class);

    logger.log("Input Received to Lambda " + input.toString());
    input.validate();

    ArrayList<String> mediums = input.setCommunicationMedium(input);

    try {
      adminGetUser = cognitoUserPool
          .adminGetUser(new AdminGetUserRequest().withUsername(getAliasFieldValue(mediums, input.getAttributes()))
              .withUserPoolId(userPoolId).withRequestCredentialsProvider(credentialsProvider));

      if (adminGetUser.getUserStatus().equals("FORCE_CHANGE_PASSWORD")) {
        if (isAnyAttributeVerified(adminGetUser)) {
          adminGetUser.getUserAttributes().parallelStream()
              .filter(attribute -> attribute.getName().contains("verified")).forEach(action -> {
                cognitoUserPool.adminUpdateUserAttributes(
                    new AdminUpdateUserAttributesRequest().withUsername(adminGetUser.getUsername())
                        .withUserAttributes(new AttributeType().withName(action.getName()).withValue("false"))
                        .withUserPoolId(userPoolId).withRequestCredentialsProvider(credentialsProvider));
              });

        }
        adminGetUser = null;
      }

    } catch (UserNotFoundException ex) {
      logger.log(input.getUsername() + " username not found ");
      // do nothing - let him register
    } catch (Exception e) {
      lambdaContext.getLogger().log("Get User Details call failed for error:" + e.getMessage());
      throw new BadRequestException("Some error occured");
    }

    if (adminGetUser != null) {
      logger.log(input.getUsername() + " username details found: " + adminGetUser.toString());
      throw new BadRequestException("Your specified " + getAliasFieldName(mediums) + " is already taken");
    }
    AdminCreateUserResult adminCreateUser;
    try {
      if (isStringEmpty(input.getSetPassword()))
        adminCreateUser = cognitoUserPool.adminCreateUser(new AdminCreateUserRequest()
            .withUserAttributes(input.getAttributes()).withUsername(input.getUsername()).withUserPoolId(userPoolId)
            .withDesiredDeliveryMediums(mediums).withRequestCredentialsProvider(credentialsProvider));
      else
        adminCreateUser = cognitoUserPool
            .adminCreateUser(new AdminCreateUserRequest().withUserAttributes(input.getAttributes())
                .withUsername(input.getUsername()).withUserPoolId(userPoolId).withDesiredDeliveryMediums(mediums)
                .withTemporaryPassword(input.getSetPassword()).withRequestCredentialsProvider(credentialsProvider));

      addingUserInUserGroup(input);
    } catch (Exception e) {
      throw new RuntimeException(getGson().toJson(e));
    }
    return getGson().toJson(adminCreateUser.getSdkHttpMetadata());
  }

  private void addingUserInUserGroup(RegisterUserRequest input) {
    try {
      cognitoUserPool.adminAddUserToGroup(new AdminAddUserToGroupRequest()
          .withGroupName(input.getUserGroup().toString()).withUsername(input.getUsername()).withUserPoolId(userPoolId)
          .withRequestCredentialsProvider(credentialsProvider));
      logger
          .log("SUCCESS: " + input.getUsername() + " has been added to User Group " + input.getUserGroup().toString());
    } catch (Exception e) {
      logger.log("ERROR: " + input.getUsername() + " has failed  in getting added to User Group "
          + input.getUserGroup().toString());
    }
  }

  private boolean isAnyAttributeVerified(AdminGetUserResult adminGetUser) {
    return adminGetUser.getUserAttributes().stream().filter(attribute -> attribute.getName().contains("verified"))
        .findFirst().orElse(null) != null;
  }

  private String getAliasFieldValue(ArrayList<String> mediums, Collection<AttributeType> attributes) {
    if (mediums.get(0).equals("EMAIL"))
      return attributes.stream().filter(attribute -> attribute.getName().equals("email")).findFirst().get().getValue();
    if (mediums.get(0).equals("SMS"))
      return attributes.stream().filter(attribute -> attribute.getName().equals("phone_number")).findFirst().get()
          .getValue();
    return null;
  }

  private String getAliasFieldName(ArrayList<String> mediums) {
    if (mediums.get(0).equals("EMAIL"))
      return "Email Address";
    else if (mediums.get(0).equals("SMS"))
      return "Phone Number";
    return null;
  }

}
