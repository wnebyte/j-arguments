package com.github.wnebyte.args;

import com.github.wnebyte.args.exception.ConstraintException;
import com.github.wnebyte.args.exception.ParseException;
import com.github.wnebyte.args.util.Reflections;
import com.github.wnebyte.args.util.Strings;
import java.util.Collection;
import java.util.Set;

/**
 * This class represents a required Argument.
 */
public class Required extends Argument {

    protected Required(
            final Set<String> name,
            final String description,
            final int index,
            final Class<?> type,
            final TypeConverter<?> typeConverter
    ) {
        super(name, description, createRegExp(name, type), index, type, typeConverter);
    }

    protected <T> Required(
            final Set<String> name,
            final String description,
            final int index,
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints
    ) {
        super(name, description, createRegExp(name, type), index, type, typeConverter, constraints);
    }

    private static String createRegExp(final Set<String> name, final Class<?> cls) {
        return "\\s" + "(" + String.join("|", name) + ")" + "\\s" +
                (Reflections.isArray(cls) ? ARRAY_VALUE_PATTERN : DEFAULT_VALUE_PATTERN);
    }

    @Override
    protected Object initialize(final String value) throws ParseException, ConstraintException {
        String val = new Splitter()
                .setName(Strings.firstSubstring(value, getNames()))
                .setValue(value)
                .split()
                .normalize(isArray())
                .get();
        return super.initialize(val);
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
        return "[" + String.join(", ", getNames()) + (isArray() ? "[...]" : "") + "]";
    }
}