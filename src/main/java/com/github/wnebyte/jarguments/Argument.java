package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.Comparator;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;
import com.github.wnebyte.jarguments.exception.ConstraintException;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Objects;
import com.github.wnebyte.jarguments.util.Reflections;
import com.github.wnebyte.jarguments.util.Sets;
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

    protected final String description;

    protected final String metavar;

    protected final Set<String> choices;

    protected final String regex;

    protected final Pattern pattern;

    protected final int index;

    protected final Class<?> type;

    protected final TypeAdapter<?> typeAdapter;

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
            final String description,
            final String metavar,
            final Set<String> choices,
            final int index,
            final Class<?> type,
            final TypeAdapter<?> typeAdapter
    ) {
        this.names = names;
        this.description = description;
        this.metavar = metavar;
        this.choices = choices;
        this.index = index;
        this.type = type;
        this.regex = createRegExp(names, type);
        this.pattern = Pattern.compile(regex);
        this.typeAdapter = typeAdapter;
        this.initializer = value -> {
            if (choices != null) {
                if (!choices.contains(value)) {
                    throw new ConstraintException(
                            String.format(
                                    "Value: '%s' is not contained within set: '%s'", value, Sets.toString(choices)
                            )
                    );
                }
            }
            return typeAdapter.convert(value);
        };
    }

    /**
     * Constructs a new instance with constraints.
     */
    public <T> Argument(
            final Set<String> names,
            final String description,
            final String metavar,
            final Set<String> choices,
            final int index,
            final Class<T> type,
            final TypeAdapter<T> typeAdapter,
            final Collection<Constraint<T>> constraints
    ) {
        this.names = names;
        this.description = description;
        this.metavar = metavar;
        this.choices = choices;
        this.index = index;
        this.type = type;
        this.regex = createRegExp(names, type);
        this.pattern = Pattern.compile(regex);
        this.typeAdapter = typeAdapter;
        this.initializer = value -> {
            if (choices != null) {
                if (!choices.contains(value)) {
                    throw new ConstraintException(
                            String.format(
                                    "Value: '%s' is not contained within set: '%s'.",  value, Sets.toString(choices)
                            )
                    );
                }
            }
            T val = typeAdapter.convert(value);
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

    protected final TypeAdapter<?> getTypeAdapter() {
        return typeAdapter;
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

    public final String getMetavar() {
        return metavar;
    }

    public final boolean hasMetavar() {
        return (metavar != null) && !(metavar.equals(Strings.EMPTY));
    }

    public final Set<String> getChoices() {
        return Collections.unmodifiableSet(choices);
    }

    public final boolean hasChoices() {
        return (choices != null) && !(choices.isEmpty());
    }

    public String getCanonicalName() {
        return (names.isEmpty()) ? null : names.toArray(new String[0])[0];
    }

    public final String getDescription() {
        return description;
    }

    public final boolean hasDescription() {
        return (description != null) && !(description.equals(Strings.EMPTY));
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
                Objects.equals(argument.description, this.description) &&
                Objects.equals(argument.metavar, this.metavar) &&
                Objects.equals(argument.choices, this.choices) &&
                Objects.equals(argument.type, this.type) &&
                Objects.equals(argument.typeAdapter, this.typeAdapter) &&
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
                Objects.hashCode(description) +
                Objects.hashCode(metavar) +
                Objects.hashCode(choices) +
                Objects.hashCode(type) +
                Objects.hashCode(typeAdapter) +
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