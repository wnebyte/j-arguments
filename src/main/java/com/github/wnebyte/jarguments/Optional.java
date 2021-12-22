package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Collection;
import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.convert.TypeConverter;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Objects;
import com.github.wnebyte.jarguments.util.Reflections;
import com.github.wnebyte.jarguments.util.Strings;

/**
 * This class represents an optional Argument.
 */
public class Optional extends Argument {

    /*
    ###########################
    #          FIELDS         #
    ###########################
    */

    protected final String defaultValue;

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    public Optional(
            final Set<String> name,
            final String description,
            final int index,
            final Class<?> type,
            final TypeConverter<?> typeConverter,
            final String defaultValue
    ) {
        super(name, description, index, type, typeConverter);
        this.defaultValue = defaultValue;
    }

    public <T> Optional(
            final Set<String> name,
            final String description,
            final int index,
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints,
            final String defaultValue
    ) {
        super(name, description, index, type, typeConverter, constraints);
        this.defaultValue = defaultValue;
    }

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    @Override
    protected String createRegExp(final Set<String> names, final Class<?> type) {
        return "(\\s" + "(" + String.join("|", names) + ")" +  "\\s" +
                (Reflections.isArray(type) ? ARRAY_VALUE_PATTERN : DEFAULT_VALUE_PATTERN) + "|)";
    }

    @Override
    protected Object initialize(final String value) throws ParseException {
        if (Strings.intersects(value, names)) {
            String val = new Splitter()
                    .setName(Strings.firstSubstring(value, names))
                    .setValue(value)
                    .split()
                    .normalize(isArray())
                    .get();
            return super.initialize(val);
        }
        else if (hasDefaultValue()) {
            String val = new Splitter()
                    .setValue(defaultValue)
                    .normalize(isArray())
                    .get();
            return typeConverter.convert(val);
        }
        else {
            return typeConverter.defaultValue();
        }
    }

    public final String getDefaultValue() {
        return defaultValue;
    }

    public final boolean hasDefaultValue() {
        return (defaultValue != null) && !(defaultValue.equals(""));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (o == this) { return true; }
        if (!(o instanceof Optional)) { return false; }
        Optional optional = (Optional) o;
        return Objects.equals(optional.defaultValue, this.defaultValue) &&
                super.equals(optional);
    }

    @Override
    public int hashCode() {
        int result = 87;
        return result +
                Objects.hashCode(this.defaultValue) +
                super.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
                "[(%s)]", String.join(" | ", names)
        );
    }

    @Override
    public String toPaddedString() {
        return String.format(
                "[( %s )]", String.join(" | ", names)
        );
    }

    @Override
    public String toDescriptiveString() {
        return "[(" + String.join(" | ", names) +
                (hasDefaultValue() ? " = ".concat(defaultValue) : "")
                + " <" + type.getSimpleName() + ">)]";
    }

    @Override
    public String toPaddedDescriptiveString() {
        return "[( " + String.join(" | ", names) +
                (hasDefaultValue() ? " = ".concat(defaultValue) : "")
                + " <" + type.getSimpleName() + "> )]";
    }
}