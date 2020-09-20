package com.sls.api.authentication.action;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.factory.ProviderFactory;
import com.sls.api.authentication.model.action.SetNewPasswordRequest;

public class SetNewPasswordAction extends AbstractAction {
  private AWSCredentialsProvider credentialsProvider = ProviderFactory.getAWS_CREDENTIALS_PROVIDER();
  private AWSCognitoIdentityProvider cognitoUserPool = ProviderFactory.getCognitoUserPoolSDK();
  private LambdaLogger logger;

  private String appClientId = ProviderFactory.getAppClientId();
  private String userPoolId = ProviderFactory.getUserPoolId();

  public String handle(String request, Context lambdaContext) throws BadRequestException {

    SetNewPasswordRequest input = getGson().fromJson(request, SetNewPasswordRequest.class);

    input.validate();
    logger = lambdaContext.getLogger();
    AdminRespondToAuthChallengeResult adminRespondToAuthChallenge = null;
    try {
      adminRespondToAuthChallenge = cognitoUserPool.adminRespondToAuthChallenge(new AdminRespondToAuthChallengeRequest()
          .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED).withSession(input.getSession())
          .withChallengeResponses(input.getChallengeParameters()).withClientId(appClientId).withUserPoolId(userPoolId)
          .withRequestCredentialsProvider(credentialsProvider));

    } catch (Exception e) {
      logger.log(e.getMessage());
      throw new RuntimeException(getGson().toJson(e));
    }

    return getGson().toJson(adminRespondToAuthChallenge);
  }
}
