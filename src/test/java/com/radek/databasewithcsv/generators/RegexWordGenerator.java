package com.radek.databasewithcsv.generators;

import com.mifmif.common.regex.Generex;

public class RegexWordGenerator {

    public static String getRandomRegexWord(String regexPattern) {
        Generex generator = new Generex(regexPattern);
        return generator.random();
    }
}
