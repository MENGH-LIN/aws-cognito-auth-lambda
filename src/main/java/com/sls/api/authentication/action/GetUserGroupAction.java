package com.sls.api.authentication.action;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminListGroupsForUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminListGroupsForUserResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.GetNameRequest;

public class GetUserGroupAction extends AbstractAction {
  private AWSCognitoIdentityProvider cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  private String userPoolId = ProviderFactory.getUserPoolId();

  public String handle(String request, Context lambdaContext) throws BadRequestException {

    GetNameRequest input = getGson().fromJson(request, GetNameRequest.class);

    input.validate();

    AdminListGroupsForUserResult adminListGroupsForUser = null;
    try {

      adminListGroupsForUser = cognitoUserPool.adminListGroupsForUser(
          new AdminListGroupsForUserRequest().withUsername(input.getUsername()).withUserPoolId(userPoolId));
    } catch (Exception e) {
      throw new RuntimeException(getGson().toJson(e));
    }

    return getGson().toJson(adminListGroupsForUser.getGroups());
  }
}
