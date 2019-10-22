package com.radek.databasewithcsv.csvhelper;

public class CsvCustomParsingException extends Exception {
    public CsvCustomParsingException() {
        super();
    }

    public CsvCustomParsingException(String message) {
        super(message);
    }

    public CsvCustomParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CsvCustomParsingException(Throwable cause) {
        super(cause);
    }

    protected CsvCustomParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
