package com.radek.databasewithcsv.csvhelper.converters;

import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstNameConverter extends NamesConverter {

    private Logger log = LoggerFactory.getLogger(FirstNameConverter.class);

    @Override
    protected Object convert(String name) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if (name.trim().isEmpty()) {
            log.error("First name cannot be null");
            throw new IllegalArgumentException("First name cannot be null");
        }
        return super.convert(name);
    }
}
