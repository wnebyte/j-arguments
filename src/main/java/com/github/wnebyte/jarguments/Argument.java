package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.converter.TypeConverter;
import com.github.wnebyte.jarguments.exception.ConstraintException;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Objects;
import com.github.wnebyte.jarguments.util.Reflections;
import java.util.Collection;
import java.util.Set;

/**
 * This class represents an abstract Argument.
 */
public abstract class Argument {

    /*
    ###########################
    #    STATIC PROPERTIES    #
    ###########################
    */
    private static final String VALUE_PATTERN = "[^\\s\"']*|\"[^\"]*\"|'[^']*'|\\$\\(.*\\)";

    protected static final String DEFAULT_VALUE_PATTERN = "(" + VALUE_PATTERN + ")";

    protected static final String ARRAY_VALUE_PATTERN = "\\[" + DEFAULT_VALUE_PATTERN + "*\\]";

    protected static final String OBJECT_VALUE_PATTERN = "\\{" + DEFAULT_VALUE_PATTERN + "*\\}";

    private static final String REQUIRED_VALUE_PATTERN =
            "(" + DEFAULT_VALUE_PATTERN + "|" + ARRAY_VALUE_PATTERN + ")";

    /*
    ###########################
    #        PROPERTIES       #
    ###########################
    */
    private final Set<String> names;

    private final String description;

    private final String regex;

    private final int index;

    private final Class<?> type;

    private final TypeConverter<?> typeConverter;

    private final Transformer<?> transformer;

    /**
     * Constructs a new instance.
     */
    public Argument(
            final Set<String> names,
            final String description,
            final int index,
            final Class<?> type,
            final TypeConverter<?> typeConverter
    ) {
        this.names = names;
        this.description = description;
        this.regex = createRegExp(names, type);
        this.index = index;
        this.type = type;
        this.typeConverter = typeConverter;
        this.transformer = (Transformer<Object>) typeConverter::convert;
    }

    /**
     * Constructs a new instance with constraints.
     */
    public <T> Argument(
            final Set<String> names,
            final String description,
            final int index,
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints
    ) {
        this.names = names;
        this.description = description;
        this.regex = createRegExp(names, type);
        this.index = index;
        this.type = type;
        this.typeConverter = typeConverter;
        this.transformer = (Transformer<Object>) value -> {
            T val = typeConverter.convert(value);
            if (constraints != null) {
                for (Constraint<T> constraint : constraints) {
                    boolean holds = constraint.holds(val);
                    if (!holds) {
                        throw new ConstraintException(
                                constraint.errorMessage()
                        );
                    }
                }
            }
            return val;
        };
    }

    private Transformer<?> getTransformer() {
        return transformer;
    }

    protected abstract String createRegExp(final Set<String> names, final Class<?> type);

    protected Object initialize(final String value) throws ParseException {
        return getTransformer().transform(value);
    }

    protected String getRegex() {
        return regex;
    }

    protected TypeConverter<?> getTypeConverter() {
        return typeConverter;
    }

    public Set<String> getNames() {
        return names;
    }

    public String getDescription() {
        return description;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isArray() {
        return Reflections.isArray(getType());
    }

    public boolean isBoolean() {
        return Reflections.isBoolean(getType());
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (o == this) { return true; }
        if (!(o instanceof Argument)) { return false; }
        Argument argument = (Argument) o;
        return Objects.equals(argument.index, this.index) &&
                Objects.equals(argument.names, this.names) &&
                Objects.equals(argument.description, this.description) &&
                Objects.equals(argument.type, this.type) &&
                Objects.equals(argument.typeConverter, this.typeConverter) &&
                Objects.equals(argument.regex, this.regex) &&
                Objects.equals(argument.transformer, this.transformer) &&
                Objects.equals(argument.getClass(), this.getClass()) &&
                super.equals(argument);
    }

    @Override
    public int hashCode() {
        int result = 31;
        return result +
                12 +
                Objects.hashCode(names) +
                Objects.hashCode(description) +
                Objects.hashCode(type) +
                Objects.hashCode(typeConverter) +
                Objects.hashCode(transformer) +
                Objects.hashCode(regex) +
                Objects.hashCode(index) +
                Objects.hashCode(getClass()) +
                super.hashCode();
    }
}