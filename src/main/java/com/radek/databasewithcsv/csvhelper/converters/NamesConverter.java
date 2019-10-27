package com.radek.databasewithcsv.csvhelper.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class NamesConverter extends AbstractBeanField {

    @Override
    protected Object convert(String name) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if (name != null) {
            return name.trim();
        }
        return name;
    }
}
