package com.sls.api.authentication.factory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

import lombok.Getter;

public class ProviderFactory {
  private static String AwsRegion;
  private static String AwsAccessKey;
  private static String AwsSecretKey;
  private static String UserPoolId;
  private static String AppClientId;
  @Getter
  private static AWSStaticCredentialsProvider AWS_CREDENTIALS_PROVIDER = new AWSStaticCredentialsProvider(
      amazonAWSCredentials());

  public static String getAppClientId() {
    if (AppClientId == null) {
      if (System.getenv("APP_CLIENT_ID") != null)
        AppClientId = System.getenv("APP_CLIENT_ID");
      else
        throw new NullPointerException("App Client Id is null.");
    }

    return AppClientId;
  }

  public static String getUserPoolId() {
    if (UserPoolId == null) {
      if (System.getenv("USER_POOL_ID") != null)
        UserPoolId = System.getenv("USER_POOL_ID");
      else
        throw new NullPointerException("User Pool is null");
    }

    return UserPoolId;
  }

  public static String getAwsAccessKey() {
    if (AwsAccessKey == null) {
      if (System.getenv("ACCESS_KEY_ID") != null)
        AwsAccessKey = System.getenv("ACCESS_KEY_ID");
      else if (System.getenv("AWS_ACCESS_KEY_ID") != null)
        AwsAccessKey = System.getenv("AWS_ACCESS_KEY_ID");
      else
        throw new NullPointerException("Aws Access Key is null");
    }

    return AwsAccessKey;
  }

  public static String getAwsSecretKey() {
    if (AwsSecretKey == null) {
      if (System.getenv("SECRET_ACCESS_KEY") != null)
        AwsSecretKey = System.getenv("SECRET_ACCESS_KEY");
      else if (System.getenv("AWS_SECRET_ACCESS_KEY") != null)
        AwsSecretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
      else
        throw new NullPointerException("Aws Secret Key is null");
    }

    return AwsSecretKey;
  }

  public static String getAwsRegion() {
    if (AwsRegion == null) {
      if (System.getenv("REGION") != null)
        AwsRegion = System.getenv("REGION");
      else if (System.getenv("AWS_REGION") != null)
        AwsRegion = System.getenv("AWS_REGION");
      else
        throw new NullPointerException("Aws Region is null");
    }

    return AwsRegion;
  }

  public static AWSCredentials amazonAWSCredentials() {
    return new BasicAWSCredentials(getAwsAccessKey(), getAwsSecretKey());
  }

  public static AWSCognitoIdentityProvider getCognitoUserPoolSDK() {
    return AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(getAWS_CREDENTIALS_PROVIDER())
        .withRegion(Regions.fromName(getAwsRegion())).withClientConfiguration(new ClientConfiguration()).build();
  }

}
