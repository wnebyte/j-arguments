package com.github.wnebyte.jarguments.util;

public class Normalizer {

    /*
    ###########################
    #      STATIC METHODS     #
    ###########################
    */

    public static String normalizeArray(final String value) {
        if (value != null) {
            return Strings.removeFirstAndLast(value, '[', ']');
        }
        return "";
    }

    public static String normalize(final String value) {
        if (value != null) {
            return value.replace("\"", "")
                    .replace("'", "");
        }
        return "";
    }

    /*
    ###########################
    #          FIELDS         #
    ###########################
    */

    private String value;

    private boolean isArray;

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    public Normalizer() {
        this(null);
    }

    public Normalizer(String value) {
        this.value = value;
    }

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    public Normalizer setValue(String value) {
        this.value = value;
        return this;
    }

    public Normalizer isArray(boolean isArray) {
        this.isArray = isArray;
        return this;
    }

    public String apply() {
        return isArray ? normalizeArray(value) : normalize(value);
    }
}
