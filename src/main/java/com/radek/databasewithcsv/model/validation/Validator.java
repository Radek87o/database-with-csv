package com.radek.databasewithcsv.model.validation;

import java.util.List;

public abstract class Validator {

    protected static void addResultOfValidation(List<String> resultList, String resultOfValidation) {
        if (resultOfValidation != null) {
            resultList.add(resultOfValidation);
        }
    }
}
