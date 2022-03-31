package com.github.wnebyte.jarguments;

import java.util.Arrays;
import java.util.Collection;
import com.github.wnebyte.jarguments.util.Pair;
import com.github.wnebyte.jarguments.util.Strings;
import static com.github.wnebyte.jarguments.util.Strings.firstAndLastEqualsAny;

/**
 * This class declares methods for retrieving and normalizing substrings from a given <code>String</code>.
 */
public class Splitter {

    /*
    ###########################
    #      STATIC FIELDS      #
    ###########################
    */

    private static final Collection<Pair<Character, Character>> PAIRS =
            Arrays.asList(new Pair<>('"', '"'), new Pair<>('\'', '\''), new Pair<>('[', ']'));

    /*
    ###########################
    #         FIELDS          #
    ###########################
    */

    private String name, value, returnVal;

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
    #         METHODS         #
    ###########################
    */

    public final Splitter setName(final String name) {
        this.name = name;
        return this;
    }

    public final Splitter setValue(final String value) {
        this.value = value;
        this.returnVal = value;
        return this;
    }

    public final Splitter split() {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Value must be non null"
            );
        }
        if (name != null) {
            returnVal = value.split(name.concat("\\s"), 2)[1];
        }
        if (!firstAndLastEqualsAny(returnVal, PAIRS)) {
            returnVal = returnVal.split("\\s", 2)[0];
        }
        return this;
    }

    public final Splitter normalize(final boolean isArray) {
        returnVal = isArray ? normalizeArray(returnVal) : normalize(returnVal);
        return this;
    }

    public final String get() {
        return returnVal;
    }
}