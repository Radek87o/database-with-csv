package com.radek.databasewithcsv.csvhelper;

import com.opencsv.bean.CsvToBeanBuilder;
import com.radek.databasewithcsv.model.AppUser;
import com.radek.databasewithcsv.model.validation.AppUserValidator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCsvReader {

    private static Logger log = LoggerFactory.getLogger(CustomCsvReader.class);

    public static List<AppUser> appUserBuilder(byte[] fileContent) throws IOException, CsvCustomParsingException {
        try {
            List<AppUser> rawData = new CsvToBeanBuilder(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileContent))))
                .withType(AppUser.class)
                .withSeparator(';')
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();
            List<AppUser> validatedAppUsers = new ArrayList<>();
            for (AppUser appUser : rawData) {
                if (AppUserValidator.validate(appUser).size() == 0) {
                    validatedAppUsers.add(appUser);
                }
            }
            log.error("Attempt to save to database {} incorrect records", rawData.size() - validatedAppUsers.size());
            return validatedAppUsers;
        } catch (RuntimeException exc) {
            log.error("An unexpected error occurred while parsing file content to object");
            throw new CsvCustomParsingException("An error occurred while parsing file content to object - remove empty lines and redundant columns");
        }
    }
}
