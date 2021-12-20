package com.github.wnebyte.jarguments;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.convert.TypeConverter;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Reflections;

/**
 * This class represents a value based Argument that has a relative position.
 */
public class Positional extends Argument implements Comparable<Positional> {

    /*
    ###########################
    #         FIELDS          #
    ###########################
    */

    private final int position;

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    public Positional(
            final String description,
            final int index,
            final Class<?> type,
            final TypeConverter<?> typeConverter,
            final int position
    ) {
        super(Collections.singleton("*"), description, index, type, typeConverter);
        this.position = position;
    }

    public <T> Positional(
            final String description,
            final int index,
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints,
            final int position
    ) {
        super(Collections.singleton("*"), description, index, type, typeConverter, constraints);
        this.position = position;
    }

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    public final int getPosition() {
        return position;
    }

    @Override
    protected String createRegExp(final Set<String> names, final Class<?> type) {
        return "\\s" + (Reflections.isArray(type) ? ARRAY_VALUE_PATTERN : DEFAULT_VALUE_PATTERN);
    }

    @Override
    protected Object initialize(final String value) throws ParseException {
        String val = new Splitter()
                .setValue(value)
                .normalize(isArray())
                .get();
        return super.initialize(val);
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
        return "[" + "..." + "]";
    }

    @Override
    public String toPaddedString() {
        return "[ " + "..." + " ]";
    }

    @Override
    public String toDescriptiveString() {
        return "[" + "... <" + getType().getSimpleName() + ">]";
    }

    @Override
    public String toPaddedDescriptiveString() {
        return "[ " + "... <" + getType().getSimpleName() + "> ]";
    }

    @Override
    public int compareTo(Positional o) {
        if (o == null) {
            throw new NullPointerException(
                    ""
            );
        }
        return position - o.position;
    }
}