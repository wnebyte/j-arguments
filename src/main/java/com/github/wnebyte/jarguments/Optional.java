package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.converter.TypeConverter;
import com.github.wnebyte.jarguments.exception.ConstraintException;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Objects;
import com.github.wnebyte.jarguments.util.Reflections;
import com.github.wnebyte.jarguments.util.Strings;
import java.util.Collection;
import java.util.Set;

/**
 * This class represents an optional Argument.
 */
public class Optional extends Argument {

    private final String defaultValue;

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

    protected String createRegExp(final Set<String> name, final Class<?> type) {
        if (Reflections.isBoolean(type)) {
            return "(\\s" + "(" + String.join("|", name) + ")" + "|)";
        }
        else {
              return "(\\s" + "(" + String.join("|", name) + ")" +  "\\s" +
                      (Reflections.isArray(type) ? ARRAY_VALUE_PATTERN : DEFAULT_VALUE_PATTERN) + "|)";
        }
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    protected Object initialize(final String value) throws ParseException, ConstraintException {
        if (Strings.intersects(value, getNames())) {
            if (isBoolean()) {
                return true;
            } else {
                String val = new Splitter()
                        .setName(Strings.firstSubstring(value, getNames()))
                        .setValue(value)
                        .split()
                        .normalize(isArray())
                        .get();
                return super.initialize(val);
            }
        }
        else {
            if (getDefaultValue() != null) {
                String val = new Splitter()
                        .setValue(getDefaultValue())
                        .normalize(isArray())
                        .get();
                return getTypeConverter().convert(val);
            }
            else {
                return getTypeConverter().defaultValue();
            }
        }
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

    // Todo: need to account for type boolean
    @Override
    public String toString() {
       // return "[" + String.join(", ", getNames()) + (isArray() ? "[...]" : "") + "]";
        return "[( " + String.join(" | ", getNames()) + " )]";
    }
}