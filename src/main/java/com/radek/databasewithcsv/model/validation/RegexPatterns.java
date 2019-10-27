package com.radek.databasewithcsv.model.validation;

import java.util.regex.Pattern;

public class RegexPatterns {
    private static Pattern namePattern = Pattern.compile("[A-Za-ząćęłńóśźżĄĘŁŃÓŚŹŻ]+$");
    private static Pattern birthDatePattern = Pattern.compile("[0-9]{4}-([0]{1}[0-9]{1}|[1]{1}[1-2]{1})-((0[1-9])|([12][0-9])|(3[01]))$");
    private static Pattern phoneNumberPattern = Pattern.compile("[4-8][0-9]{8}$");

    static boolean matchesNamePattern(String name) {
        return namePattern.matcher(name).matches();
    }

    static boolean matchesBirthDatePattern(String birthDate) {
        return birthDatePattern.matcher(birthDate).matches();
    }

    static boolean matchesPhoneNumberPattern(String phoneNumber) {
        return phoneNumberPattern.matcher(phoneNumber).matches();
    }
}
