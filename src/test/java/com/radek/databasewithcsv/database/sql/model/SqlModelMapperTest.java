package com.radek.databasewithcsv.database.sql.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.radek.databasewithcsv.model.AppUser;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

class SqlModelMapperTest {

    private SqlModelMapper sqlModelMapper = Mappers.getMapper(SqlModelMapper.class);

    private static Stream<Arguments> appUserBuilderParameters() {
        return Stream.of(
            Arguments.of("Ewa", "Kowalska", LocalDate.of(1968, 10, 23), "567123741"),
            Arguments.of("Jan", "Nowak", LocalDate.of(1990, 12, 2), "724452423"),
            Arguments.of("Grzegorz", "Brzęczyszczykiewicz", LocalDate.of(1925, 7, 9), "500600700"),
            Arguments.of("Anna", "Zielińska", LocalDate.of(2002, 5, 11), "646121343")
        );
    }

    private static Stream<Arguments> sqlappUserBuilderParameters() {
        return Stream.of(
            Arguments.of(1L, "Ewa", "Kowalska", LocalDate.of(1968, 10, 23), "567123741"),
            Arguments.of(2L, "Jan", "Nowak", LocalDate.of(1990, 12, 2), "724452423"),
            Arguments.of(3L, "Grzegorz", "Brzęczyszczykiewicz", LocalDate.of(1925, 7, 9), "500600700"),
            Arguments.of(4L, "Anna", "Zielińska", LocalDate.of(2002, 5, 11), "646121343")
        );
    }

    @ParameterizedTest
    @MethodSource("appUserBuilderParameters")
    void shouldMapAppUserToSqlAppUser(String firstName, String lastName, LocalDate birthDate, String phoneNumber) {
        AppUser exampleUser = AppUser.builder()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();

        com.radek.databasewithcsv.database.sql.model.AppUser sqlExampleAppUser = sqlModelMapper.toSqlAppUser(exampleUser);

        assertEquals(exampleUser.getFirstName(), sqlExampleAppUser.getFirstName());
        assertEquals(exampleUser.getLastName(), sqlExampleAppUser.getLastName());
        assertEquals(exampleUser.getBirthDate(), sqlExampleAppUser.getBirthDate());
        assertEquals(exampleUser.getPhoneNumber(), sqlExampleAppUser.getPhoneNumber());
    }

    @ParameterizedTest
    @MethodSource("sqlappUserBuilderParameters")
    void shouldMapSqlAppUserToAppUser(Long id, String firstName, String lastName, LocalDate birthDate, String phoneNumber) {
        com.radek.databasewithcsv.database.sql.model.AppUser sqlAppUser = com.radek.databasewithcsv.database.sql.model.AppUser.builder()
            .withId(id)
            .withFirstName(firstName)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withPhoneNumber(phoneNumber)
            .build();

        AppUser appUser = sqlModelMapper.toAppUser(sqlAppUser);

        assertEquals(sqlAppUser.getId(), appUser.getId());
        assertEquals(sqlAppUser.getFirstName(), appUser.getFirstName());
        assertEquals(sqlAppUser.getLastName(), appUser.getLastName());
        assertEquals(sqlAppUser.getBirthDate(), appUser.getBirthDate());
        assertEquals(sqlAppUser.getPhoneNumber(), appUser.getPhoneNumber());
    }
}