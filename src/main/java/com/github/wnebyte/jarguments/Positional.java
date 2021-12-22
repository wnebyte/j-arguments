package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.convert.TypeConverter;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Reflections;

/**
 * This class represents a positional Argument.
 */
public class Positional extends Argument implements Comparable<Positional> {

    /*
    ###########################
    #         FIELDS          #
    ###########################
    */

    protected final int position;

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

    public final int getPosition() {
        return position;
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
        return String.format(
                "[$R%d]", position
        );
    }

    @Override
    public String toPaddedString() {
        return String.format(
                "[ $R%d ]", position
        );
    }

    @Override
    public String toDescriptiveString() {
        return String.format(
                "[$R%d <%s>]", position, type.getSimpleName()
        );
    }

    @Override
    public String toPaddedDescriptiveString() {
        return String.format(
                "[ $R%d <%s> ]", position, type.getSimpleName()
        );
    }

    @Override
    public String toGenericString() {
        return String.format(
                "$R%d", position
        );
    }

    @Override
    public int compareTo(Positional o) {
        if (o == null) {
            throw new NullPointerException(
                    "The specified Positional Argument must not be null."
            );
        }
        return position - o.position;
    }
}