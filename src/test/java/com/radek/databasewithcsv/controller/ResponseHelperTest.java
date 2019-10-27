package com.radek.databasewithcsv.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.radek.databasewithcsv.generators.AppUserGenerator;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class ResponseHelperTest {

    @ParameterizedTest
    @MethodSource("jsonOkResponseArguments")
    void shouldCreateJsonResponseWithOkStatus(Object body) {
        ResponseEntity<?> expected = createExpectedResponse(body, MediaType.APPLICATION_JSON, HttpStatus.OK);
        ResponseEntity<?> response = ResponseHelper.createJsonOkResponse(body);
        assertEquals(expected, response);
    }

    private static Stream<Arguments> jsonOkResponseArguments() {
        return Stream.of(
            Arguments.of(AppUserGenerator.generateAppUser()),
            Arguments.of(AppUserGenerator.generateAppUserWithId(45L)),
            Arguments.of(AppUserGenerator.generateAppUserWithLastName("Kowalski")),
            Arguments.of(AppUserGenerator.generateAppUserWithSpecificBirthDate("1987-09-14")),
            Arguments.of("Any example String")
        );
    }

    @Test
    void createJsonOkResponseMethodShouldThrowExceptionWhenNullIsPassed() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ResponseHelper.createJsonOkResponse(null));
        assertEquals("Response body cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("jsonCreatedResponseArguments")
    void shouldCreateJsonResponseWithCreatedStatus(Object body) {
        ResponseEntity<?> expected = createExpectedResponse(body, MediaType.APPLICATION_JSON, HttpStatus.CREATED);
        ResponseEntity<?> response = ResponseHelper.createJsonCreatedResponse(body);
        assertEquals(expected, response);
    }

    private static Stream<Arguments> jsonCreatedResponseArguments() {
        return Stream.of(
            Arguments.of(AppUserGenerator.generateAppUser()),
            Arguments.of(AppUserGenerator.generateAppUser()),
            Arguments.of("Any not null object"),
            Arguments.of(AppUserGenerator.generateAppUserWithId(2L))
        );
    }

    @Test
    void createJsonCreatedResponseMethodShouldThrowExceptionWhenNullIsPassedAsBody() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ResponseHelper.createJsonCreatedResponse(null));
        assertEquals("Response body cannot be null", exception.getMessage());
    }

    private ResponseEntity<?> createExpectedResponse(Object body, MediaType mediaType, HttpStatus status) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(mediaType);
        return new ResponseEntity<>(body, responseHeaders, status);
    }
}