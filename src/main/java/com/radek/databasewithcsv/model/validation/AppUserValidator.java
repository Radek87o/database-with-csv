package com.radek.databasewithcsv.model.validation;

import com.radek.databasewithcsv.model.AppUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppUserValidator extends Validator {
    private static Logger log = LoggerFactory.getLogger(AppUserValidator.class);

    public static List<String> validate(AppUser appUser) {
        if (appUser == null) {
            return Collections.singletonList("App user cannot be null");
        }
        List<String> result = new ArrayList<>();
        addResultOfValidation(result, validateFirstName(appUser.getFirstName()));
        addResultOfValidation(result, validateLastName(appUser.getLastName()));
        addResultOfValidation(result, validateBirthDate(appUser.getBirthDate()));
        addResultOfValidation(result, validatePhoneNumber(appUser.getPhoneNumber()));
        return result;
    }

    private static String validateFirstName(String firstName) {
        if (firstName == null || firstName.length() == 0) {
            log.error("First name cannot be null");
            return "First name cannot be null";
        }
        if (!RegexPatterns.matchesNamePattern(firstName)) {
            log.error("First name should contain only letters");
            return "First name should contain only letters";
        }
        return null;
    }

    private static String validateLastName(String lastName) {
        if (lastName == null || lastName.length() == 0) {
            log.error("Last name cannot be null");
            return "Last name cannot be null";
        }
        if (!RegexPatterns.matchesNamePattern(lastName)) {
            log.error("Last name should contain only letters");
            return "Last name should contain only letters";
        }
        return null;
    }

    private static String validateBirthDate(String birthDate) {
        if (birthDate == null || birthDate.length() == 0) {
            log.error("Birth date cannot be null");
            return "Birth date cannot be null";
        }
        if (!RegexPatterns.matchesBirthDatePattern(birthDate)) {
            log.error("Incorrect format of birth date");
            return "Incorrect format of birth date";
        }
        return null;
    }

    private static String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 0 && !RegexPatterns.matchesPhoneNumberPattern(phoneNumber)) {
            log.error("Incorrect format of phone number");
            return "Incorrect format of phone number";
        }
        return null;
    }
}
