package com.radek.databasewithcsv.csvhelper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.radek.databasewithcsv.model.AppUser;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CsvReaderIT {

    private static final String INPUT_PATH = "src/test/resources/csvHelper/%s.csv";
    private CsvReader reader;

    @BeforeEach
    public void setup() {
        reader = new CsvReader();
    }

    @Test
    void shouldCsvReaderCorrectlyMapFirstNameValueToAppUserClass() throws FileNotFoundException, CsvCustomParsingException {
        List<AppUser> appUsersFromFile = reader.appUserBuilder(String.format(INPUT_PATH, "correct"));
        assertEquals("Stefan", appUsersFromFile.get(0).getFirstName());
        assertEquals("Maria", appUsersFromFile.get(1).getFirstName());
        assertEquals("Jolanta", appUsersFromFile.get(2).getFirstName());
    }

    @Test
    void shouldCsvReaderCorrectlyMapLastNameValueToAppUserClass() throws FileNotFoundException, CsvCustomParsingException {
        List<AppUser> appUsersFromFile = reader.appUserBuilder(String.format(INPUT_PATH, "correct"));
        assertEquals("Testowy", appUsersFromFile.get(0).getLastName());
        assertEquals("Ziółko", appUsersFromFile.get(1).getLastName());
        assertEquals("Magia", appUsersFromFile.get(2).getLastName());
    }

    @Test
    void shouldCsvReaderCorrectlyMapBirthDateValueToAppUserClass() throws FileNotFoundException, CsvCustomParsingException {
        List<AppUser> appUsersFromFile = reader.appUserBuilder(String.format(INPUT_PATH, "correct"));
        assertEquals(LocalDate.of(1988, 11, 11), appUsersFromFile.get(0).getBirthDate());
        assertEquals(LocalDate.of(1999, 1, 10), appUsersFromFile.get(1).getBirthDate());
        assertEquals(LocalDate.of(2000, 2, 4), appUsersFromFile.get(2).getBirthDate());
    }

    @Test
    void shouldCsvReaderCorrectlyMapPhoneNumberValueToAppUserClass() throws FileNotFoundException, CsvCustomParsingException {
        List<AppUser> appUsersFromFile = reader.appUserBuilder(String.format(INPUT_PATH, "correct"));
        assertEquals("600700800", appUsersFromFile.get(0).getPhoneNumber());
        assertEquals("", appUsersFromFile.get(1).getPhoneNumber());
        assertEquals("666000111", appUsersFromFile.get(2).getPhoneNumber());
    }

    /*@ParameterizedTest
    @MethodSource("setOfExceptionMessages")
    void shouldAppUserBuilderMethodThrowExceptionWhenParametersAreIncorrect(String fileName, String message) {
        CsvCustomParsingException exception = assertThrows(CsvCustomParsingException.class, () -> {
            reader.appUserBuilder(String.format(INPUT_PATH, fileName));
        }, message);
        assertEquals(message, exception.getMessage());
    }

    private static Stream<Arguments> setOfExceptionMessages() {
        return Stream.of(
            Arguments.of("nullFirstName", "First name cannot be null"),
            Arguments.of("nullLastName", "Last name cannot be null"),
            Arguments.of("incorrectBirthDate", "Incorrect format of birth date"),
            Arguments.of("incorrectPhoneNumber", "Incorrect phone number format")
        );
    }*/
}