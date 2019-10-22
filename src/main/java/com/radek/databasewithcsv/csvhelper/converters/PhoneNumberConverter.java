package com.radek.databasewithcsv.csvhelper.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhoneNumberConverter extends AbstractBeanField {

    private static Pattern phoneNumberPattern = Pattern.compile("[0-9]{9}$");
    private Logger log = LoggerFactory.getLogger(PhoneNumberConverter.class);

    @Override
    protected Object convert(String phoneNumber) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if (!phoneNumberPattern.matcher(phoneNumber).matches() && !phoneNumber.isEmpty()) {
            log.error("Incorrect phone number format");
            throw new IllegalArgumentException("Incorrect phone number format");
        }
        return phoneNumber;
    }
}
