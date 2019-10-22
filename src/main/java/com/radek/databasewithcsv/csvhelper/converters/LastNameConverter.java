package com.radek.databasewithcsv.csvhelper.converters;

import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LastNameConverter extends NamesConverter {

    private Logger log = LoggerFactory.getLogger(LastNameConverter.class);

    @Override
    protected Object convert(String name) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if (name.trim().isEmpty()) {
            log.error("Last name cannot be null");
            throw new IllegalArgumentException("Last name cannot be null");
        }
        return super.convert(name);
    }
}
