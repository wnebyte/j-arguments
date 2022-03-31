package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.Comparator;
import com.github.wnebyte.jarguments.convert.TypeConverter;
import com.github.wnebyte.jarguments.exception.ConstraintException;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Objects;
import com.github.wnebyte.jarguments.util.Reflections;
import com.github.wnebyte.jarguments.util.Strings;

/**
 * This class represents an abstract Argument.
 */
public abstract class Argument implements Comparable<Argument> {

    /*
    ###########################
    #      STATIC FIELDS      #
    ###########################
    */

    private static final String VALUE_PATTERN = "[^\\s\"']*|\"[^\"]*\"|'[^']*'";

    protected static final String DEFAULT_VALUE_PATTERN = "(" + VALUE_PATTERN + ")";

    protected static final String ARRAY_VALUE_PATTERN = "\\[" + DEFAULT_VALUE_PATTERN + "*\\]";

    protected static final Comparator<Argument> COMPARATOR = Comparator.comparing(Argument::getIndex);

    /*
    ###########################
    #         FIELDS          #
    ###########################
    */

    protected final Set<String> names;

    protected final String desc;

    protected final String regex;

    protected final Pattern pattern;

    protected final int index;

    protected final Class<?> type;

    protected final TypeConverter<?> typeConverter;

    protected final Initializer<Object> initializer;

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    /**
     * Constructs a new instance.
     */
    public Argument(
            final Set<String> names,
            final String desc,
            final int index,
            final Class<?> type,
            final TypeConverter<?> typeConverter
    ) {
        this.names = names;
        this.desc = desc;
        this.index = index;
        this.type = type;
        this.regex = createRegExp(names, type);
        this.pattern = Pattern.compile(regex);
        this.typeConverter = typeConverter;
        this.initializer = typeConverter::convert;
    }

    /**
     * Constructs a new instance with constraints.
     */
    public <T> Argument(
            final Set<String> names,
            final String desc,
            final int index,
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints
    ) {
        this.names = names;
        this.desc = desc;
        this.index = index;
        this.type = type;
        this.regex = createRegExp(names, type);
        this.pattern = Pattern.compile(regex);
        this.typeConverter = typeConverter;
        this.initializer = value -> {
            T val = typeConverter.convert(value);
            if (constraints != null) {
                for (Constraint<T> constraint : constraints) {
                    if (!constraint.verify(val)) {
                        throw new ConstraintException(
                                constraint.errorMessage()
                        );
                    }
                }
            }
            return val;
        };
    }

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    protected abstract String createRegExp(final Set<String> names, final Class<?> type);

    protected Object initialize(final String value) throws ParseException {
        return initializer.apply(value);
    }

    protected final TypeConverter<?> getTypeConverter() {
        return typeConverter;
    }

    protected final boolean matches(String keyValue) {
        return pattern.matcher(keyValue).matches();
    }

    protected final String getRegex() {
        return regex;
    }

    protected final Pattern getPattern() {
        return pattern;
    }

    public final Set<String> getNames() {
        return Collections.unmodifiableSet(names);
    }

    public String getCanonicalName() {
        return (names.isEmpty()) ? null : names.toArray(new String[0])[0];
    }

    public final String getDescription() {
        return desc;
    }

    public final boolean hasDescription() {
        return (desc != null) && !(desc.equals(Strings.EMPTY));
    }

    public final Class<?> getType() {
        return type;
    }

    public final boolean isArray() {
        return Reflections.isArrayOrIterable(type);
    }

    public final boolean isBoolean() {
        return Reflections.isBoolean(type);
    }

    public final int getIndex() {
        return index;
    }

    @Override
    public int compareTo(Argument o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (o == this) { return true; }
        if (!(o instanceof Argument)) { return false; }
        Argument argument = (Argument) o;
        return Objects.equals(argument.index, this.index) &&
                Objects.equals(argument.names, this.names) &&
                Objects.equals(argument.desc, this.desc) &&
                Objects.equals(argument.type, this.type) &&
                Objects.equals(argument.typeConverter, this.typeConverter) &&
                Objects.equals(argument.regex, this.regex) &&
                Objects.equals(argument.initializer, this.initializer) &&
                Objects.equals(argument.getClass(), this.getClass()) &&
                super.equals(argument);
    }

    @Override
    public int hashCode() {
        int result = 31;
        return result +
                12 +
                Objects.hashCode(names) +
                Objects.hashCode(desc) +
                Objects.hashCode(type) +
                Objects.hashCode(typeConverter) +
                Objects.hashCode(initializer) +
                Objects.hashCode(regex) +
                Objects.hashCode(index) +
                Objects.hashCode(getClass()) +
                super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String toPaddedString() {
        return toString();
    }

    public String toDescriptiveString() {
        return toString();
    }

    public String toPaddedDescriptiveString() {
        return toDescriptiveString();
    }

}