package com.mazzone.isere.transport.util;

import java.util.Locale;

public class Strings {

    public static String capitalizeFirstLetterEachWord(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return WordUtil.capitalize(original.toLowerCase(Locale.getDefault()));
    }
}
