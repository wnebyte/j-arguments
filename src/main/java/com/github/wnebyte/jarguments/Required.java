package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Collection;
import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.convert.TypeConverter;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Reflections;
import com.github.wnebyte.jarguments.util.Strings;

/**
 * This class represents a required Argument.
 */
public class Required extends Argument {

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

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

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    @Override
    protected String createRegExp(final Set<String> name, final Class<?> type) {
        return "\\s" + "(" + String.join("|", name) + ")" + "\\s" +
                (isArray() ? ARRAY_VALUE_PATTERN : DEFAULT_VALUE_PATTERN);
    }

    @Override
    protected Object initialize(final String value) throws ParseException {
        String val = new Splitter()
                .setName(Strings.firstSubstring(value, names))
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
        return String.format(
                "[%s]", String.join(" | ", names)
        );
    }

    @Override
    public String toPaddedString() {
        return String.format(
                "[ %s ]", String.join(" | ", names)
        );
    }

    @Override
    public String toDescriptiveString() {
        return String.format(
                "[%s <%s>]", String.join(" | ", names), type.getSimpleName()
        );
    }

    @Override
    public String toPaddedDescriptiveString() {
        return String.format(
                "[ %s <%s> ]", String.join(" | ", names), type.getSimpleName()
        );
    }
}