package com.github.wnebyte.args;

import com.github.wnebyte.args.exception.ConstraintException;
import com.github.wnebyte.args.exception.ParseException;
import com.github.wnebyte.args.util.Reflections;
import java.util.Collection;
import java.util.Collections;

/**
 * This class represents a positional Argument.
 */
public class Positional extends Argument {

    private final int position;

    protected Positional(
            final String description,
            final int index,
            final Class<?> type,
            final TypeConverter<?> typeConverter,
            final int position
    ) {
        super(Collections.singleton("*"), description, createRegExp(type), index, type, typeConverter);
        this.position = position;
    }

    protected <T> Positional(
            final String description,
            final int index,
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints,
            final int position
    ) {
        super(Collections.singleton("*"), description, createRegExp(type), index, type, typeConverter, constraints);
        this.position = position;
    }

    private static String createRegExp(final Class<?> cls) {
        return "\\s" + (Reflections.isArray(cls) ? ARRAY_VALUE_PATTERN : DEFAULT_VALUE_PATTERN);
    }

    public int getPosition() {
        return position;
    }

    @Override
    protected Object initialize(final String value) throws ParseException, ConstraintException {
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
        return "[*" + (Reflections.isArray(getType()) ? "[...]" : "") + "]";
    }

}