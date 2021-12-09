package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.converter.TypeConverter;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Reflections;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * This class represents a positional Argument.
 */
public class Positional extends Argument {

    private final int position;

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

    public int getPosition() {
        return position;
    }

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
        return positional.position == this.position &&
                super.equals(positional);
    }

    @Override
    public int hashCode() {
        int result = 66;
        return position +
                result +
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

}