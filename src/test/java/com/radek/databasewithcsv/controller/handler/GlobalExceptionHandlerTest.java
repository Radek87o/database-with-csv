package com.radek.databasewithcsv.controller.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

class GlobalExceptionHandlerTest {

    GlobalExceptionHandler handler;
    WebRequest request;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
        request = Mockito.mock(WebRequest.class);
    }

    @ParameterizedTest
    @MethodSource("statusExceptionsArguments")
    void shouldReturnResponseWithCorrectStatusAndBodyWhenResponseStatusExceptionIsThrown(ResponseStatusException exception) {
        ResponseEntity<Object> response = handler.handleUnexpectedException(exception, request);
        String stringBody = response.getBody().toString();
        assertEquals(exception.getReason(), extractMessageFromResponseBody(stringBody));
        assertEquals(exception.getStatus(), response.getStatusCode());
    }

    private static Stream<Arguments> statusExceptionsArguments() {
        return Stream.of(
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing file parameter")),
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file format")),
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to add file without any correct app user")),
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to provide null page number")),
            Arguments.of(new ResponseStatusException(HttpStatus.NOT_FOUND, "App user for provided id doesn't exist")),
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get app users by last name without providing any name.")),
            Arguments.of(new ResponseStatusException(HttpStatus.NOT_FOUND, "Id cannot be null")),
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "App user with provided id doesn't exist"))
        );
    }

    @Test
    void shouldReturnJsonResponseWithCorrectStatusAndBodyWhenUnexpectedErrorOccur() {
        ResponseEntity<Object> response = handler.handleUnexpectedException(new NullPointerException(), request);
        String stringBody = response.getBody().toString();
        assertEquals("An unexpected error occurred", extractMessageFromResponseBody(stringBody));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private String extractMessageFromResponseBody(String body) {
        int startIndex = body.indexOf("message=");
        int endIndex = body.indexOf(", path=");
        return body.substring(startIndex, endIndex).replaceFirst("message=", "");
    }
}