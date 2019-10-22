package com.radek.databasewithcsv.csvhelper.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDateConverter extends AbstractBeanField {

    private static Pattern sixDigitsDate = Pattern.compile("[0-9]{4}.[1-9]{1}.[1-9]{1}");
    private static Pattern oneDigitMonthDate = Pattern.compile("[0-9]{4}.[1-9]{1}.[0-3]{1}[0-9]{1}$");
    private static Pattern oneDigitDayDate = Pattern.compile("[0-9]{4}.[0-1]{1}[0-9]{1}.[1-9]{1}$");
    private static Pattern correctDatePattern = Pattern.compile("[0-9]{4}.[0-1]{1}[0-9]{1}.[0-3]{1}[0-9]{1}$");
    private Logger log = LoggerFactory.getLogger(LocalDateConverter.class);

    @Override
    protected Object convert(String dateInput) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        String adjustedDateInput = adjustDateInput(dateInput);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return LocalDate.parse(adjustedDateInput, formatter);
    }

    private String adjustDateInput(String dateInput) {
        StringBuilder dateBuilder = new StringBuilder(dateInput);
        if (sixDigitsDate.matcher(dateInput).matches()) {
            return dateBuilder.insert(5, "0").insert(8, "0").toString();
        }
        if (oneDigitDayDate.matcher(dateInput).matches()) {
            return dateBuilder.insert(8, "0").toString();
        }
        if (oneDigitMonthDate.matcher(dateInput).matches()) {
            return dateBuilder.insert(5, "0").toString();
        }
        if (correctDatePattern.matcher(dateInput).matches()) {
            return dateInput;
        }
        log.error("Incorrect format of birth date");
        throw new IllegalArgumentException("Incorrect format of birth date");
    }
}
