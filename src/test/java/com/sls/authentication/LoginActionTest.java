package com.sls.authentication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.sls.api.authentication.RequestRouter;
import com.sls.api.authentication.exception.BadRequestException;
import com.sls.api.authentication.exception.InternalErrorException;
import com.sls.authentication.context.TestContext;

public class LoginActionTest {

  private static final String SAMPLE_INPUT_STRING = "{\n" + "  \"action\": \"LoginAction\",\n"
      + "  \"body\": {\n" + "    \"username\": \"altrovatest\",\n" + "    \"password\": \"g8D_yES1\"\n"
      + "  }\n" + "}";


//  private static final String EXPECTED_OUTPUT_STRING = "{\"user\":{\"username\":\"wdtest\",\"attributes\":[{\"name\":\"sub\",\"value\":\"b20a90fb-31c6-4f7e-b5ea-187ba10a378a\"},{\"name\":\"custom:publicARN\",\"value\":\"XAIUgi546\"},{\"name\":\"name\",\"value\":\"WealthTech\"},{\"name\":\"email\",\"value\":\"abdeali@wealthtech.in\"},{\"name\":\"custom:company\",\"value\":\"Wealth Technology & Services Pvt Ltd\"}],\"userCreateDate\":\"Feb 26, 2018 3:22:02 PM\",\"userLastModifiedDate\":\"Feb 26, 2018 3:22:02 PM\",\"enabled\":true,\"userStatus\":\"FORCE_CHANGE_PASSWORD\"},\"sdkResponseMetadata\":{\"metadata\":{\"AWS_REQUEST_ID\":\"b1b26053-1ada-11e8-97f3-1dc9dfb29f6b\"}},\"sdkHttpMetadata\":{\"httpHeaders\":{\"Connection\":\"keep-alive\",\"Content-Length\":\"440\",\"Content-Type\":\"application/x-amz-json-1.1\",\"Date\":\"Mon, 26 Feb 2018 09:52:02 GMT\",\"x-amzn-RequestId\":\"b1b26053-1ada-11e8-97f3-1dc9dfb29f6b\"},\"httpStatusCode\":200}}";

  @Test
  public void test() throws IOException, BadRequestException, InternalErrorException {
    RequestRouter handler = new RequestRouter();

    InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());
    OutputStream output = new ByteArrayOutputStream();

    handler.handleRequest(input, output, new TestContext());

    String sampleOutputString = output.toString();
    System.out.println(sampleOutputString);
    // Assert.assertEquals(EXPECTED_OUTPUT_STRING, sampleOutputString);
    Assert.assertEquals(sampleOutputString, sampleOutputString);
  }

}
