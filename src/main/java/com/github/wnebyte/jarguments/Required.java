package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Collection;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Normalizer;

/**
 * This class represents an initialize able required Argument.
 */
public class Required extends Argument {

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    public <T> Required(
            final Set<String> name,
            final String description,
            final String metavar,
            final Set<String> choices,
            final int index,
            final Class<T> type,
            final TypeAdapter<T> typeAdapter,
            final Collection<Constraint<T>> constraints
    ) {
        super(name, description, metavar, choices, index, type, typeAdapter, constraints);
    }

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    @Override
    protected String pattern(final Set<String> name, final Class<?> type) {
        return "(" + String.join("|", name) + ")" + "\\s" +
                (isArray() ? ARRAY_VALUE_PATTERN : DEFAULT_VALUE_PATTERN);
    }

    @Override
    protected Object initialize(final String value) throws ParseException {
        String val = new Normalizer()
                .setValue(value)
                .isArray(isArray())
                .apply();
        return initializer.apply(val);
    }

    @Override
    public int compareTo(Argument o) {
        if (o instanceof Positional) {
            return -1;
        } else if (o instanceof Required) {
            return index - o.index;
        }
        else {
            return +1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (o == this) { return true; }
        if (!(o instanceof Required)) { return false; }
        Required required = (Required) o;
        return super.equals(required);
    }

    @Override
    public int hashCode() {
        int result = 45;
        return result +
                super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}