package com.github.wnebyte.jarguments;

import java.util.Collection;
import java.util.Set;
import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.converter.TypeConverter;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Objects;
import com.github.wnebyte.jarguments.util.Strings;

/**
 * This class represents an optional Argument that only consists of a name, and can be initialized into one of two
 * values.
 */
public final class Flag extends Optional {

    /**
     * The value to use during initialization when included.
     */
    private final String value;

    public Flag(
            final Set<String> names,
            final String description,
            final int index,
            final Class<?> type,
            final TypeConverter<?> typeConverter,
            final String value,
            final String defaultValue
    ) {
        super(names, description, index, type, typeConverter, defaultValue);
        this.value = value;
    }

    public <T> Flag(
            final Set<String> name,
            final String description,
            final int index,
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints,
            final String value,
            final String defaultValue
    ) {
        super(name, description, index, type, typeConverter, constraints, defaultValue);
        this.value = value;
    }

    @Override
    protected String createRegExp(final Set<String> names, final Class<?> type) {
        return "(\\s" + "(" + String.join("|", names) + ")" + "|)";
    }

    @Override
    protected Object initialize(final String value) throws ParseException {
        if (Strings.intersects(value, getNames())) {
            return getTypeConverter().convert(this.value);
        } else {
            return hasDefaultValue() ?
                    getTypeConverter().convert(getDefaultValue()) : getTypeConverter().defaultValue();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof Flag))
            return false;
        Flag flag = (Flag) o;
        return Objects.equals(flag.value, this.value) &&
                super.equals(flag);
    }

    @Override
    public int hashCode() {
        int result = 12;
        return 4 * result + Objects.hashCode(value) + super.hashCode();
    }

    @Override
    public String toString() {
        return "[(" + String.join(" | ", getNames()) + ")]";
    }

    @Override
    public String toPaddedString() {
        return "[( " + String.join(" | ", getNames()) + " )]";
    }

    @Override
    public String toDescriptiveString() {
        return toString();
    }

    @Override
    public String toPaddedDescriptiveString() {
        return toPaddedString();
    }
}