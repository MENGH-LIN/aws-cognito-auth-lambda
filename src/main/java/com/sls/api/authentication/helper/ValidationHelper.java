package com.sls.api.authentication.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationHelper {

  public static boolean isStringEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
      "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
      Pattern.CASE_INSENSITIVE);

  public static boolean isValidEmail(String emailStr) {
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
    return matcher.find();
  }

  public static boolean isValidPhoneNumber(String phoneNumber) {
    if (phoneNumber.length() != 13)
      return false;
    try {
      String number = phoneNumber.substring(phoneNumber.length() - 10);
      Long.parseLong(number);
    } catch (NumberFormatException e) {
      return false;
    }

    if (!phoneNumber.substring(0, 3).equals("+91"))
      return false;
    return true;
  }

  public static boolean hasSpecialCharacters(String value) {
    Pattern regex = Pattern.compile("[a-zA-Z0-9]");
    Matcher matcher = regex.matcher(value);
    return !matcher.find();
  }

  public static boolean hasSpace(String value) {
    return value.contains(" ");
  }

  public static boolean isValidUsername(String username) {
    if (hasSpecialCharacters(username))
      return false;
    return !username.contains("__");
  }

  public static boolean isValidPanCard(String pancard) {
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    Matcher matcher = pattern.matcher(pancard);
    return matcher.matches();
  }

}
