package com.github.wnebyte.jarguments;

import java.util.*;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;
import com.github.wnebyte.jarguments.exception.ParseException;

/**
 * This class represents a positional and required <code>Argument</code> that has only a value.
 */
public class Positional extends Required {

    /*
    ###########################
    #         FIELDS          #
    ###########################
    */

    protected final int position;

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    public <T> Positional(
            final String description,
            final String metavar,
            final Set<String> choices,
            final int index,
            final Class<T> type,
            final TypeAdapter<T> typeAdapter,
            final Collection<Constraint<T>> constraints,
            final int position
    ) {
        super(null, description, metavar, choices, index, type, typeAdapter, constraints);
        this.position = position;
    }

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    @Override
    protected String pattern(final Set<String> names, final Class<?> type) {
        return isArray() ? ARRAY_VALUE_PATTERN : DEFAULT_VALUE_PATTERN;
    }

    @Override
    protected Object initialize(final String value) throws ParseException {
        String val = new Splitter()
                .setValue(value)
                .normalize(isArray())
                .get();
        return initializer.apply(val);
    }

    public final int getPosition() {
        return position;
    }

    @Override
    public int compareTo(Argument o) {
        if (o instanceof Positional) {
            return position - ((Positional) o).position;
        } else {
            return index - o.getIndex();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (o == this) { return true; }
        if (!(o instanceof Positional)) { return false; }
        Positional positional = (Positional) o;
        return Objects.equals(positional.position, this.position) &&
                super.equals(positional);
    }

    @Override
    public int hashCode() {
        int result = 66;
        return result +
                Objects.hashCode(position) +
                super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}