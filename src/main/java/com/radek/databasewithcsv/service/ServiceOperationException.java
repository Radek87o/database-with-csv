package com.radek.databasewithcsv.service;

public class ServiceOperationException extends Exception {

    public ServiceOperationException() {
        super();
    }

    public ServiceOperationException(String message) {
        super(message);
    }

    public ServiceOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceOperationException(Throwable cause) {
        super(cause);
    }
}
