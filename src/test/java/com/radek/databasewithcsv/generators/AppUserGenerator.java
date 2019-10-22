package com.radek.databasewithcsv.generators;

import com.radek.databasewithcsv.model.AppUser;

import java.time.LocalDate;

public class AppUserGenerator {

    private static final String NAME_REGEX_PATTERN = "[A-Z]{1}[a-z]{1,7}";
    private static final String PHONE_NUMBER_PATTERN = "[4-8]{1}[0-9]{8}";

    public static AppUser generateAppUser() {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String lastName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        LocalDate birthDate = LocalDate.now();
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
        LocalDate birthDate = LocalDate.now();
        String phoneNumber = RegexWordGenerator.getRandomRegexWord(PHONE_NUMBER_PATTERN);

        return AppUser.builder()
            .withId(id)
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();
    }

    public static AppUser generateAppUserWithSpecificBirthDate(LocalDate birthDate) {
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

    public static AppUser generateAppUserWithoutPhoneNumber() {
        String firstName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        String lastName = RegexWordGenerator.getRandomRegexWord(NAME_REGEX_PATTERN);
        LocalDate birthDate = LocalDate.now();

        return AppUser.builder()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .build();
    }
}
