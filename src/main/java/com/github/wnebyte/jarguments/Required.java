package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.converter.TypeConverter;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Reflections;
import com.github.wnebyte.jarguments.util.Strings;
import java.util.Collection;
import java.util.Set;

/**
 * This class represents a required Argument.
 */
public class Required extends Argument {

    public Required(
            final Set<String> name,
            final String description,
            final int index,
            final Class<?> type,
            final TypeConverter<?> typeConverter
    ) {
        super(name, description, index, type, typeConverter);
    }

    public <T> Required(
            final Set<String> name,
            final String description,
            final int index,
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints
    ) {
        super(name, description, index, type, typeConverter, constraints);
    }

    protected String createRegExp(final Set<String> name, final Class<?> type) {
        return "\\s" + "(" + String.join("|", name) + ")" + "\\s" +
                (Reflections.isArray(type) ? ARRAY_VALUE_PATTERN : DEFAULT_VALUE_PATTERN);
    }

    @Override
    protected Object initialize(final String value) throws ParseException {
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
       // return "[" + String.join(", ", getNames()) + (isArray() ? "[...]" : "") + "]";
        return "[ " + String.join(" | ", getNames()) + " ]";
    }
}