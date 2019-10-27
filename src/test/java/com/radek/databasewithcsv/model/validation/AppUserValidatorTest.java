package com.radek.databasewithcsv.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.radek.databasewithcsv.generators.AppUserGenerator;
import com.radek.databasewithcsv.model.AppUser;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AppUserValidatorTest {

    @Test
    void shouldValidateAppUser() {
        List<String> resultOfValidation = AppUserValidator.validate(null);
        assertEquals(List.of("App user cannot be null"), resultOfValidation);
    }

    @ParameterizedTest
    @MethodSource("setOfFirstNamesAndValidationResults")
    void shouldValidateFirstName(String firstName, List<String> expected) {
        AppUser appUserToValidate = AppUserGenerator.generateAppUserWithFirstName(firstName);
        List<String> resultOfValidation = AppUserValidator.validate(appUserToValidate);
        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> setOfFirstNamesAndValidationResults() {
        return Stream.of(
            Arguments.of("Jan", List.of()),
            Arguments.of("Zdzisław", List.of()),
            Arguments.of("Józef", List.of()),
            Arguments.of(null, List.of("First name cannot be null")),
            Arguments.of("@#WEadarw@#$", List.of("First name should contain only letters"))
        );
    }

    @ParameterizedTest
    @MethodSource("setOfLastNamesAndValidationResults")
    void shouldValidateLastName(String lastName, List<String> expected) {
        AppUser appUserToValidate = AppUserGenerator.generateAppUserWithLastName(lastName);
        List<String> resultOfValidation = AppUserValidator.validate(appUserToValidate);
        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> setOfLastNamesAndValidationResults() {
        return Stream.of(
            Arguments.of("Nowak", List.of()),
            Arguments.of("Wiśniewski", List.of()),
            Arguments.of("Gżegżółka", List.of()),
            Arguments.of(null, List.of("Last name cannot be null")),
            Arguments.of("XDAX$@#!#", List.of("Last name should contain only letters"))
        );
    }

    @ParameterizedTest
    @MethodSource("setOfBirthDatesAndValidationResults")
    void shouldValidateBirthDate(String birthDate, List<String> expected) {
        AppUser appUserToValidate = AppUserGenerator.generateAppUserWithSpecificBirthDate(birthDate);
        List<String> resultOfValidation = AppUserValidator.validate(appUserToValidate);
        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> setOfBirthDatesAndValidationResults() {
        return Stream.of(
            Arguments.of("1934-01-22", List.of()),
            Arguments.of("1978-12-31", List.of()),
            Arguments.of("2003-01-02", List.of()),
            Arguments.of("", List.of("Birth date cannot be null")),
            Arguments.of("data", List.of("Incorrect format of birth date")),
            Arguments.of("1999-1-1", List.of("Incorrect format of birth date")),
            Arguments.of("997-10-13", List.of("Incorrect format of birth date")),
            Arguments.of("1967-04-32", List.of("Incorrect format of birth date")),
            Arguments.of("1971-13-31", List.of("Incorrect format of birth date"))
        );
    }

    @ParameterizedTest
    @MethodSource("setOfPhoneNumbersAndValidationResults")
    void shouldValidatePhoneNumber(String phoneNumber, List<String> expected) {
        AppUser appUserToValidate = AppUserGenerator.generateAppUserWithPhoneNumber(phoneNumber);
        List<String> resultOfValidation = AppUserValidator.validate(appUserToValidate);
        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> setOfPhoneNumbersAndValidationResults() {
        return Stream.of(
            Arguments.of("", List.of()),
            Arguments.of("444134742", List.of()),
            Arguments.of("765135853", List.of()),
            Arguments.of("numer", List.of("Incorrect format of phone number")),
            Arguments.of("345678912", List.of("Incorrect format of phone number")),
            Arguments.of("7654321987", List.of("Incorrect format of phone number")),
            Arguments.of("971-136-311", List.of("Incorrect format of phone number"))
        );
    }
}