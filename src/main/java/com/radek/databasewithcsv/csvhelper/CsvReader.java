package com.radek.databasewithcsv.csvhelper;

import com.opencsv.bean.CsvToBeanBuilder;
import com.radek.databasewithcsv.model.AppUser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvReader {

    private Logger log = LoggerFactory.getLogger(CsvReader.class);

    public List<AppUser> appUserBuilder(String fileName) throws FileNotFoundException, CsvCustomParsingException {
        try {
            return new CsvToBeanBuilder(new FileReader(fileName, Charset.forName("UTF-8")))
                .withType(AppUser.class)
                .withSeparator(';')
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();
        } catch (RuntimeException | IOException exc) {
            throw new CsvCustomParsingException("An error occurred while parsing file to object");
        }
    }
}
