package com.radek.databasewithcsv.generators;

import com.radek.databasewithcsv.model.AppUser;

public class AppUserGenerator {

    private static final String NAME_REGEX_PATTERN = "[A-Za-ząćęłńóśźżĄĘŁŃÓŚŹŻ]{6,}";
    private static final String DATE_PATTERN = "[0-9]{4}-([0]{1}[1-9]{1}|[1]{1}[1-2]{1})-((0[1-9])|([12][0-8]))";
    private static final String PHONE_NUMBER_PATTERN = "[4-8][0-9]{8}";

    public static AppUser generateAppUser() {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String lastName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String birthDate = RegexWordGenerator.getRandomRegexWord(DATE_PATTERN);
        String phoneNumber = RegexWordGenerator.getRandomRegexWord(PHONE_NUMBER_PATTERN);

        return AppUser.builder()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();
    }

    public static AppUser generateAppUserWithId(Long id) {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String lastName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String birthDate = RegexWordGenerator.getRandomRegexWord(DATE_PATTERN);
        String phoneNumber = RegexWordGenerator.getRandomRegexWord(PHONE_NUMBER_PATTERN);

        return AppUser.builder()
            .withId(id)
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();
    }

    public static AppUser generateAppUserWithSpecificBirthDate(String birthDate) {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String lastName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String phoneNumber = RegexWordGenerator.getRandomRegexWord(PHONE_NUMBER_PATTERN);

        return AppUser.builder()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();
    }

    public static AppUser generateAppUserWithSpecificIdAndBirthDate(Long id, String birthDate) {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String lastName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String phoneNumber = RegexWordGenerator.getRandomRegexWord(PHONE_NUMBER_PATTERN);

        return AppUser.builder()
            .withId(id)
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();
    }

    public static AppUser generateAppUserWithFirstName(String firstName) {
        String lastName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String birthDate = RegexWordGenerator.getRandomRegexWord(DATE_PATTERN);
        String phoneNumber = RegexWordGenerator.getRandomRegexWord(PHONE_NUMBER_PATTERN);

        return AppUser.builder()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();
    }

    public static AppUser generateAppUserWithLastName(String lastName) {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String birthDate = RegexWordGenerator.getRandomRegexWord(DATE_PATTERN);
        String phoneNumber = RegexWordGenerator.getRandomRegexWord(PHONE_NUMBER_PATTERN);

        return AppUser.builder()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();
    }

    public static AppUser generateAppUserWithLastNameAndBirthDate(String lastName, String birthDate) {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String phoneNumber = RegexWordGenerator.getRandomRegexWord(PHONE_NUMBER_PATTERN);

        return AppUser.builder()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();
    }

    public static AppUser generateAppUserWithPhoneNumber(String phoneNumber) {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String lastName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String birthDate = RegexWordGenerator.getRandomRegexWord(DATE_PATTERN);

        return AppUser.builder()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();
    }

    public static AppUser generateAppUserWithoutPhoneNumber() {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String lastName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String birthDate = RegexWordGenerator.getRandomRegexWord(DATE_PATTERN);

        return AppUser.builder()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .build();
    }

    public static AppUser generateAppUserWithSpecificBirthDateAndWithoutPhoneNumber(String birthDate) {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String lastName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);

        return AppUser.builder()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber("")
            .build();
    }
}
