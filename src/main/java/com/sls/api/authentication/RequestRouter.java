package com.sls.api.authentication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sls.api.authentication.action.Action;
import com.sls.api.authentication.exception.RuntimeServerException;

public class RequestRouter implements RequestStreamHandler {

  Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  @Override
  public void handleRequest(InputStream request, OutputStream response, Context context) throws IOException {
    LambdaLogger logger = context.getLogger();
    JsonObject inputObj;
    try {
      String requestString = getStringFromStream(request);
      inputObj = gson.fromJson(requestString, JsonObject.class);
    } catch (Exception e) {
      throw new RuntimeServerException(new Gson().toJson(e.getMessage()));
    }

    if (inputObj == null || inputObj.get("action") == null || inputObj.get("action").getAsString().trim().equals("")) {
      logger.log("Invald inputObj, could not find action parameter");
      throw new RuntimeServerException(gson.toJson("Could not find which action to execute as per the request"));
    }

    String actionClass = inputObj.get("action").getAsString();
    Action action;

    try {
      action = Action.class.cast(Class.forName("com.sls.api.authentication.action." + actionClass).newInstance());
    } catch (final Exception e) {
      logger.log("Action class could not be created \n" + e.getMessage());
      logger.log(Arrays.stream(e.getStackTrace()).map(err -> err.toString()).collect(Collectors.joining("\n")));
      throw new RuntimeServerException(gson.toJson(e.getMessage()), e);
    }

    if (action == null) {
      logger.log("Action class is null");
      throw new RuntimeServerException("Action class is null");
    }

    JsonObject body = null;
    if (inputObj.get("body") != null) {
      body = inputObj.get("body").getAsJsonObject();
    }

    String bodyText = body.toString();
    String output = action.handle(bodyText, context);
    logger.log("API lambda response" + output + " for incoming json request " + bodyText);

    try {
      response.write(output.getBytes());
    } catch (final Exception e) {
      logger.log("Error while writing response to output stream \n" + e.getMessage());
      throw new RuntimeServerException("Could write to output stream", e);
    }
  }

  public static String getStringFromStream(InputStream request) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[1024];
    while ((nRead = request.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }

    buffer.flush();
    byte[] byteArray = buffer.toByteArray();

    return new String(byteArray, StandardCharsets.UTF_8);
  }

  public static void main(String[] args) {
    System.out.println("Application Started");
  }

}
