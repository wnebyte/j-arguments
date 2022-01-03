package com.github.wnebyte.jarguments.factory;

import java.util.*;
import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.convert.AbstractTypeConverterMap;
import com.github.wnebyte.jarguments.convert.TypeConverterMap;
import com.github.wnebyte.jarguments.convert.TypeConverter;
import com.github.wnebyte.jarguments.util.Reflections;
import com.github.wnebyte.jarguments.util.Strings;

public class ArgumentFactory extends AbstractArgumentFactory {

    /*
    ###########################
    #      STATIC METHODS     #
    ###########################
    */

    /**
     * @return a new builder instance.
     */
    public static ArgumentFactoryBuilder builder() {
        return new ArgumentFactoryBuilder();
    }

    private static boolean namesIsNull(final Set<String> names, final Class<? extends Argument> cls) {
        if (cls == Positional.class) {
            return false;
        }
        else {
            return (names == null) || (names.isEmpty());
        }
    }

    /*
    ###########################
    #          FIELDS         #
    ###########################
    */

    private final Collection<Character> exclude = new ArrayList<Character>() {{
        add(' ');
        add('"');
        add('\'');
        add('[');
        add(']');
        add(',');
        add('$');
        add('(');
        add(')');
        add('{');
        add('}');
    }};

    private final AbstractTypeConverterMap typeConverters;

    private final Set<String> allNames = new HashSet<>();

    private final List<Argument> arguments = new ArrayList<>();

    /*
    ###########################
    #     ARGUMENT FIELDS     #
    ###########################
    */

    private Set<String> names;

    private String description;

    private Class<? extends Argument> subClass;

    private Class<?> type;

    private TypeConverter<?> typeConverter;

    private int index = 0;

    private int position = 0;

    private String flagValue;

    private String defaultValue;

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    /**
     * Constructs a new instance using the specified <code>exclude</code> and <code>typeConverters</code>.
     */
    public ArgumentFactory(
            Collection<Character> exclude,
            AbstractTypeConverterMap typeConverters
    ) {
        this.exclude.addAll(exclude);
        this.typeConverters = (typeConverters != null) ? typeConverters : TypeConverterMap.getInstance();
    }

    @Override
    public Collection<Character> getExcludeCharacters() {
        return Collections.unmodifiableCollection(exclude);
    }

    /**
     * Sets the name property of the next argument to be appended.
     * @param names of the next argument.
     * @return this.
     */
    public ArgumentFactory setName(final String... names) {
        if ((names == null) || (names.length == 0)) {
            throw new IllegalArgumentException(
                    "Names may not be null/empty."
            );
        }
        this.names = new LinkedHashSet<>(names.length);
        for (String n : names) {
            n = Strings.removeAll(n, exclude);
            if (n.equals("")) {
                throw new IllegalArgumentException(
                        String.format(
                                "The name of an Argument must not be left empty after having been normalized. " +
                                        "The following characters are removed during normalization: %s.",
                                Arrays.toString(exclude.toArray())
                        )
                );
            }
            boolean distinct = this.names.add(n) & allNames.add(n);
            if (!distinct) {
                throw new IllegalArgumentException(
                        String.format(
                                "An Argument with name: '%s' has already been appended.", n
                        )
                );
            }
        }
        return this;
    }

    /**
     * Sets the type property of the next argument to be appended.
     * @param type of the next argument.
     * @return this.
     */
    public ArgumentFactory setType(final Class<?> type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the typeConverter property of the next argument to be appended.
     * @param typeConverter of the next argument.
     * @return this.
     */
    public ArgumentFactory setTypeConverter(final TypeConverter<?> typeConverter) {
        this.typeConverter = typeConverter;
        return this;
    }

    /**
     * Sets the description property of the next argument to be appended.
     * @param description of the next argument.
     * @return this.
     */
    public ArgumentFactory setDescription(final String description) {
        this.description = description;
        return this;
    }

    public ArgumentFactory setSubClass(final Class<? extends Argument> sClass) {
        this.subClass = sClass;
        return this;
    }

    /**
     * Sets the defaultValue property of the next argument to be appended.
     * @param defaultValue of the next (optional/flag) argument.
     * @return this.
     */
    public ArgumentFactory setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ArgumentFactory setFlagValue(final String flagValue) {
        this.flagValue = flagValue;
        return this;
    }

    /**
     * Specifies that the next argument to be appended is of type {@link Required}.
     * @return this (for chaining).
     */
    public ArgumentFactory setIsRequired() {
        this.subClass = Required.class;
        return this;
    }

    /**
     * Specifies that the next argument to be appended is of type {@link Optional}.
     * @return this (for chaining).
     */
    public ArgumentFactory setIsOptional() {
        this.subClass = Optional.class;
        return this;
    }

    /**
     * Specifies that the next argument to be appended is of type {@link Positional}.
     * @return this (for chaining).
     */
    public ArgumentFactory setIsPositional() {
        this.subClass = Positional.class;
        return this;
    }

    /**
     * Specifies that the next argument to be appended is of type {@link Flag}.
     * @return this (for chaining).
     */
    public ArgumentFactory setIsFlag() {
        this.subClass = Flag.class;
        return this;
    }

    /**
     * Creates a new argument using the specified <code>type</code>.
     * @return this.
     */
    public <T> ArgumentFactory append(final Class<T> type) {
        TypeConverter<T> typeConverter = typeConverters.get(type);
        return append(type, typeConverter);
    }
    /**
     * Creates a new argument using the specified <code>type</code> and <code>typeConverter</code>.
     * @return this.
     */
    public <T> ArgumentFactory append(final Class<T> type, final TypeConverter<T> typeConverter) {
        return append(type, typeConverter, null);
    }

    public <T> ArgumentFactory append(final Class<T> type, final Collection<Constraint<T>> constraints) {
        TypeConverter<T> typeConverter = typeConverters.get(type);
        return append(type, typeConverter, constraints);
    }

    public <T> ArgumentFactory append(
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints

    ) {
        final Argument argument;

        if ((type == null) || (typeConverter == null)) {
            throw new IllegalArgumentException(
                    "Type & TypeConverter have to be specified."
            );
        }
        if (subClass == null) {
            subClass = Reflections.isBoolean(type) ? Flag.class : Required.class;
        }
        if (namesIsNull(names, subClass)) {
            throw new IllegalArgumentException(
                    "At least one name has to be specified for each non-positional Argument."
            );
        }
        if (Reflections.isBoolean(type) && subClass == Flag.class) {
            argument = new Flag(
                    names,
                    description,
                    index++,
                    type,
                    typeConverter,
                    flagValue == null ? "true" : flagValue,
                    defaultValue
            );
        }
        else if (Flag.class.equals(subClass)) {
            argument = new Flag(
                    names,
                    description,
                    index++,
                    type,
                    typeConverter,
                    Objects.requireNonNull(flagValue, () ->
                            "FlagValue has to be specified for instances of Flag if type is not of type boolean."),
                    defaultValue
            );
        }
        else if (Optional.class.equals(subClass)) {
            argument = new Optional(
                    names,
                    description,
                    index++,
                    type,
                    typeConverter,
                    constraints,
                    defaultValue
            );
        }
        else if (Required.class.equals(subClass)) {
            argument = new Required(
                    names,
                    description,
                    index++,
                    type,
                    typeConverter,
                    constraints
            );
        }
        else if (Positional.class.equals(subClass)) {
            argument = new Positional(
                    description,
                    index++,
                    type,
                    typeConverter,
                    constraints,
                    position++
            );
        }
        else {
            throw new IllegalArgumentException(
                    String.format(
                            "This factory instance does not know how to construct instances of class: %s.", subClass
                    )
            );
        }

        arguments.add(argument);
        reset();
        return this;
    }

    public ArgumentFactory append() {
        final Argument argument;

        if (namesIsNull(names, subClass)) {
            throw new IllegalArgumentException(
                    "At least one name has to be specified for each non-positional Argument."
            );
        }
        if (type == null) {
            throw new IllegalArgumentException(
                    "Type has to be specified."
            );
        }
        if (typeConverter == null) {
            typeConverter = typeConverters.get(type);

            if (typeConverter == null) {
                throw new IllegalArgumentException(
                        String.format(
                                "This factory instance's TypeConverterMap has no mapping for Type: %s.", type
                        )
                );
            }
        }
        if (subClass == null) {
            subClass = Reflections.isBoolean(type) ? Flag.class : Required.class;
        }
        if (Reflections.isBoolean(type) && subClass == Flag.class) {
            argument = new Flag(
                    names,
                    description,
                    index++,
                    type,
                    typeConverter,
                    flagValue == null ? "true" : flagValue,
                    defaultValue
            );
        }
        else if (Flag.class.equals(subClass)) {
            argument = new Flag(
                    names,
                    description,
                    index++,
                    type,
                    typeConverter,
                    Objects.requireNonNull(flagValue,
                            "FlagValue has to be specified for instances of Flag if type is not of type boolean."),
                    defaultValue
            );
        }
        else if (Optional.class.equals(subClass)) {
            argument = new Optional(
                    names,
                    description,
                    index++,
                    type,
                    typeConverter,
                    defaultValue
            );
        }
        else if (Required.class.equals(subClass)) {
            argument = new Required(
                    names,
                    description,
                    index++,
                    type,
                    typeConverter
            );
        }
        else if (Positional.class.equals(subClass)) {
            argument = new Positional(
                    description,
                    index++,
                    type,
                    typeConverter,
                    position++
            );
        }
        else {
            throw new IllegalArgumentException(
                    String.format(
                            "This factory instance does not know how to construct instances of class: %s.", subClass
                    )
            );
        }

        arguments.add(argument);
        reset();
        return this;
    }

    private void reset() {
        names = null;
        defaultValue = null;
        flagValue = null;
        description = null;
        subClass = null;
        type = null;
        typeConverter = null;
    }

    public List<Argument> get() {
        List<Argument> arguments = new ArrayList<>(this.arguments);
        this.arguments.clear();
        index = 0;
        position = 0;
        reset();
        arguments.sort(Argument::compareTo);
        return arguments;
    }
}