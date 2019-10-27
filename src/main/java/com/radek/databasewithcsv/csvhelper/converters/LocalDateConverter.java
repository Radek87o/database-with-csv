package com.radek.databasewithcsv.csvhelper.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.regex.Pattern;

public class LocalDateConverter extends AbstractBeanField {

    private static Pattern sixDigitsDate = Pattern.compile("[0-9]{4}.[1-9]{1}.[1-9]{1}");
    private static Pattern oneDigitMonthDate = Pattern.compile("[0-9]{4}.[1-9]{1}.[0-3]{1}[0-9]{1}$");
    private static Pattern oneDigitDayDate = Pattern.compile("[0-9]{4}.[0-1]{1}[0-9]{1}.[1-9]{1}$");
    private static Pattern correctDatePattern = Pattern.compile("[0-9]{4}.[0-1]{1}[0-9]{1}.[0-3]{1}[0-9]{1}$");

    @Override
    protected Object convert(String dateInput) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        String adjustedDateInput = adjustDateInput(dateInput);
        if (adjustedDateInput != null) {
            return adjustedDateInput.replace(".", "-");
        }
        return adjustedDateInput;
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
        return null;
    }
}
