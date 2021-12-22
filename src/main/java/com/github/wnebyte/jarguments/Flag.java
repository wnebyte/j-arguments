package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Collection;
import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.convert.TypeConverter;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Objects;
import com.github.wnebyte.jarguments.util.Strings;

/**
 * This class represents an optional Argument that has both a predefined value for when it's included, and excluded.
 */
public class Flag extends Optional {

    /*
    ###########################
    #         FIELDS          #
    ###########################
    */

    /**
     * The value to use during initialization when the option has been included.
     */
    protected final String value;

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

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

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    @Override
    protected String createRegExp(final Set<String> names, final Class<?> type) {
        return "(\\s" + "(" + String.join("|", names) + ")" + "|)";
    }

    @Override
    protected Object initialize(final String value) throws ParseException {
        if (Strings.intersects(value, names)) {
            return typeConverter.convert(this.value);
        } else {
            return hasDefaultValue() ?
                    typeConverter.convert(defaultValue) : typeConverter.defaultValue();
        }
    }

    public final String getValue() {
        return value;
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
        return toString();
    }

    @Override
    public String toPaddedDescriptiveString() {
        return toPaddedString();
    }
}