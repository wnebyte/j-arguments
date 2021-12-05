package com.github.wnebyte.jarguments;

import java.util.Arrays;
import com.github.wnebyte.jarguments.util.Pair;
import com.github.wnebyte.jarguments.util.Strings;
import static com.github.wnebyte.jarguments.util.Strings.firstAndLastMatchesAny;

public class Splitter {

    private String name, value, val;

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

    public final Splitter setName(final String name) {
        this.name = name;
        return this;
    }

    public final Splitter setValue(final String value) {
        this.value = value;
        this.val = value;
        return this;
    }

    public final Splitter split() {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Value must be non null"
            );
        }
        if (name != null) {
            val = value.split(name.concat("\\s"), 2)[1];
        }
        if (!firstAndLastMatchesAny(
                val,
                Arrays.asList(new Pair<>('"', '"'), new Pair<>('\'', '\''), new Pair<>('[', ']')))
        ) {
            val = val.split("\\s", 2)[0];
        }
        return this;
    }

    public final Splitter normalize(final boolean isArray) {
        val = isArray ? normalizeArray(val) : normalize(val);
        return this;
    }

    public final String get() {
        return val;
    }
}