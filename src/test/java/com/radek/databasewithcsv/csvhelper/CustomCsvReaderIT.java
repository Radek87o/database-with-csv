
package com.radek.databasewithcsv.csvhelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.radek.databasewithcsv.model.AppUser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomCsvReaderIT {

    private static final String INPUT_PATH = "src/test/resources/csvHelper/%s.csv";
    private CustomCsvReader reader;

    @BeforeEach
    public void setup() {
        reader = new CustomCsvReader();
    }

    @Test
    void shouldCsvReaderCorrectlyMapFirstNameValueToAppUserClass() throws IOException, CsvCustomParsingException {
        List<AppUser> appUsersFromFile = readAppUsersFromFile("correct");
        assertEquals("Stefan", appUsersFromFile.get(0).getFirstName());
        assertEquals("Maria", appUsersFromFile.get(1).getFirstName());
        assertEquals("Jolanta", appUsersFromFile.get(2).getFirstName());
    }

    @Test
    void shouldCsvReaderCorrectlyMapLastNameValueToAppUserClass() throws IOException, CsvCustomParsingException {
        List<AppUser> appUsersFromFile = readAppUsersFromFile("correct");
        assertEquals("Testowy", appUsersFromFile.get(0).getLastName());
        assertEquals("Ziółko", appUsersFromFile.get(1).getLastName());
        assertEquals("Magia", appUsersFromFile.get(2).getLastName());
    }

    @Test
    void shouldCsvReaderCorrectlyMapBirthDateValueToAppUserClass() throws IOException, CsvCustomParsingException {
        List<AppUser> appUsersFromFile = readAppUsersFromFile("correct");
        assertEquals(LocalDate.of(1988, 11, 11).toString(), appUsersFromFile.get(0).getBirthDate());
        assertEquals(LocalDate.of(1999, 1, 1).toString(), appUsersFromFile.get(1).getBirthDate());
        assertEquals(LocalDate.of(2000, 2, 4).toString(), appUsersFromFile.get(2).getBirthDate());
    }

    @Test
    void shouldCsvReaderCorrectlyMapPhoneNumberValueToAppUserClass() throws IOException, CsvCustomParsingException {
        List<AppUser> appUsersFromFile = readAppUsersFromFile("correct");
        assertEquals("600700800", appUsersFromFile.get(0).getPhoneNumber());
        assertEquals("", appUsersFromFile.get(1).getPhoneNumber());
        assertEquals("666000111", appUsersFromFile.get(2).getPhoneNumber());
    }

    @Test
    void shouldAppUserBuilderMethodThrowExceptionWhenFileContainsEmptyLines() throws IOException {
        byte[] fileContent = Files.readAllBytes(Path.of(String.format(INPUT_PATH, "emptyLine")));
        CsvCustomParsingException exception = assertThrows(CsvCustomParsingException.class, () -> {
            reader.appUserBuilder(fileContent);
        });
        assertEquals("An error occurred while parsing file content to object - remove empty lines and redundant columns", exception.getMessage());
    }

    @Test
    void shouldAppUserBuilderMethodThrowExceptionWhenFileContainsUnnecessaryColumns() throws IOException {
        byte[] fileContent = Files.readAllBytes(Path.of(String.format(INPUT_PATH, "incorrectNumberOfColumns")));
        CsvCustomParsingException exception = assertThrows(CsvCustomParsingException.class, () -> {
            reader.appUserBuilder(fileContent);
        });
        assertEquals("An error occurred while parsing file content to object - remove empty lines and redundant columns", exception.getMessage());
    }

    private List<AppUser> readAppUsersFromFile(String fileName) throws IOException, CsvCustomParsingException {
        byte[] fileContent = Files.readAllBytes(Path.of(String.format(INPUT_PATH, "correct")));
        return reader.appUserBuilder(fileContent);
    }
}
