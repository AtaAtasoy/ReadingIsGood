package com.ataatasoy.readingisgood.helpers;

import java.util.regex.Pattern;

public class Validator {
    /**
     * Pattern taken from
     * https://stackoverflow.com/questions/8204680/java-regex-email
     * Said to be in compliance of RFC 5322
     * 
     * @param emailString the email to be validated
     * @return 'true' if valid, else 'false'
     */
    //TODO: 
    public static boolean isValidEmail(String emailString) {
        return Pattern.matches("^[a-zA-Z0-9_!#$%&'*+/=?``{|}~^.-]+@[a-zA-Z0-9.-]+$", emailString);
    }
}
