package com.sls.api.authentication.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sls.api.authentication.model.action.RegisterInvestorUIClientRequest;

public class PrepareUserConfirmationToWealthDeskRequest {
  private String path;
  private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  public String getRegisterRequestJsonString(AdminGetUserResult user, LambdaLogger logger) {
    HashMap<String, String> requestToServer = new HashMap<String, String>();
    List<AttributeType> userAttributes = user.getUserAttributes();

    userAttributes.forEach(attribute -> {
      if (attribute.getName().equals("phone_number")) {
        requestToServer.put("phoneNumber", attribute.getValue());
      } else if (attribute.getName().equals("custom:company")) {
        requestToServer.put("company", attribute.getValue());
      } else if (attribute.getName().equals("custom:publicARN")) {
        requestToServer.put("publicARN", attribute.getValue());
      } else if (!(attribute.getName().equals("custom:isMigrated") || attribute.getName().equals("sub"))) {
        requestToServer.put(attribute.getName(), attribute.getValue());
      }
    });

    if (requestToServer.getOrDefault("company", null) == null)
      path = "/registerinvestorfromuserpool";
    else
      path = "/registeradviserfromuserpool";

    String username = user.getUsername();
    if (path.equals("/registerinvestorfromuserpool")) {
      requestToServer.put("username", username);
    } else if (path.equals("/registeradviserfromuserpool")) {
      requestToServer.put("adviserUsername", username);
    }

    if (requestToServer.get("phoneNumber") == null) {
      requestToServer.put("phoneNumber", "");
    }
    if (requestToServer.get("email") == null) {
      requestToServer.put("email", "");
    }

    return gson.toJson(requestToServer);

  }

  public String getKYCRequestJsonString(RegisterInvestorUIClientRequest input, LambdaLogger logger) {
    Map<String, String> attributes = new HashMap<String, String>();
    attributes.put("username", input.getUsername());
    attributes.put("panCard", input.getPanCard());
    attributes.put("clientCode", input.getClientCode());
    attributes.put("brokerCompany", input.getBrokerName());
    attributes.put("phoneNumber", input.getPhoneNumber());
    attributes.put("email", input.getEmail());
    return gson.toJson(attributes);
  }
}
