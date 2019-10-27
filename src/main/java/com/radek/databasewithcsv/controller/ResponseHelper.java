package com.radek.databasewithcsv.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {

    public static ResponseEntity<?> createJsonOkResponse(Object body) {
        if (body == null) {
            throw new IllegalArgumentException("Response body cannot be null");
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
    }

    public static ResponseEntity<?> createJsonCreatedResponse(Object body) {
        if (body == null) {
            throw new IllegalArgumentException("Response body cannot be null");
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, responseHeaders, HttpStatus.CREATED);
    }
}
