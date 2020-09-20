package com.sls.api.authentication.action;

import java.io.IOException;

import com.amazonaws.services.lambda.runtime.Context;

public interface Action {
  String handle(String request, Context lambdaContext) throws IOException;
}