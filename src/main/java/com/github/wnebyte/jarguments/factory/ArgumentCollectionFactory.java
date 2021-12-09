package com.github.wnebyte.jarguments.factory;

import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.converter.AbstractTypeConverterMap;
import com.github.wnebyte.jarguments.converter.TypeConverter;
import com.github.wnebyte.jarguments.converter.TypeConverterMap;
import com.github.wnebyte.jarguments.util.Reflections;
import com.github.wnebyte.jarguments.util.Strings;

import java.util.*;

/**
 * This class declares methods for building multiple instances of subclasses of the {@link Argument} class.
 */
public class ArgumentCollectionFactory extends AbstractArgumentCollectionFactory {

    /*
    ###########################
    #    INSTANCE VARIABLES   #
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
    #   ARGUMENT PROPERTIES   #
    ###########################
    */
    private Set<String> names;

    private String description;

    private Class<? extends Argument> subClass;

    private Class<?> type;

    private TypeConverter<?> typeConverter;

    private int index = 0;

    private int position = 0;

    private String defaultValue;

    /**
     * Constructs a new instance using the specified <code>exclude</code> and <code>typeConverters</code>.
     */
    public ArgumentCollectionFactory(
            Collection<Character> exclude, AbstractTypeConverterMap typeConverters
    ) {
        this.exclude.addAll(exclude);
        this.typeConverters = (typeConverters != null) ?
                typeConverters : TypeConverterMap.getInstance();
    }

    @Override
    public Collection<Character> getExcludeCharacters() {
        return new ArrayList<>(exclude);
    }

    /**
     * Sets the name property of the next argument to be created.
     * @param names of the next argument.
     * @return this.
     */
    public ArgumentCollectionFactory setNames(final String... names) {
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
                        "The name of an Argument may not be left empty after normalization. " +
                                "The following characters are removed during normalization: " +
                                Arrays.toString(exclude.toArray()) + "."
                );
            }
            boolean success = this.names.add(n) & allNames.add(n);
            if (!success) {
                throw new IllegalArgumentException(
                        "An Argument with the name: '" + n + "'" +
                                " has already been constructed using this instance."
                );
            }
        }
        return this;
    }

    /**
     * Sets the type property of the next argument to be created.
     * @param type of the next argument.
     * @return this.
     */
    public ArgumentCollectionFactory setType(final Class<?> type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the typeConverter property of the next argument to be created.
     * @param typeConverter of the next argument.
     * @return this.
     */
    public ArgumentCollectionFactory setTypeConverter(final TypeConverter<?> typeConverter) {
        this.typeConverter = typeConverter;
        return this;
    }

    /**
     * Sets the description property of the next argument to be created.
     * @param description of the next argument.
     * @return this.
     */
    public ArgumentCollectionFactory setDescription(final String description) {
        this.description = description;
        return this;
    }

    public ArgumentCollectionFactory setSubClass(final Class<? extends Argument> sClass) {
        this.subClass = sClass;
        return this;
    }

    /**
     * Sets the defaultValue property of the next argument to be created.
     * @param defaultValue of the next (optional) argument.
     * @return this.
     */
    public ArgumentCollectionFactory setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Specifies that the next argument to be created is to be a required argument.
     * @return this.
     */
    public ArgumentCollectionFactory setIsRequired() {
        this.subClass = Required.class;
        return this;
    }

    /**
     * Specifies that the next argument to be created is to be an optional argument.
     * @return this.
     */
    public ArgumentCollectionFactory setIsOptional() {
        this.subClass = com.github.wnebyte.jarguments.Optional.class;
        return this;
    }

    /**
     * Specifies that the next argument to be created is to be a positional argument.
     * @return this.
     */
    public ArgumentCollectionFactory setIsPositional() {
        this.subClass = Positional.class;
        return this;
    }

    /**
     * Creates a new argument using the specified <code>type</code>.
     * @return this.
     */
    public <T> ArgumentCollectionFactory append(final Class<T> type) {
        TypeConverter<T> typeConverter = typeConverters.get(type);
        return append(type, typeConverter);
    }
    /**
     * Creates a new argument using the specified <code>type</code> and <code>typeConverter</code>.
     * @return this.
     */
    public <T> ArgumentCollectionFactory append(final Class<T> type, final TypeConverter<T> typeConverter) {
        return append(type, typeConverter, null);
    }

    public <T> ArgumentCollectionFactory append(final Class<T> type, final Collection<Constraint<T>> constraints) {
        TypeConverter<T> typeConverter = typeConverters.get(type);
        return append(type, typeConverter, constraints);
    }

    public <T> ArgumentCollectionFactory append(
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints

    ) {
        Argument argument;

        if (namesIsNull(names, subClass)) {
            throw new IllegalArgumentException(
                    "Names has to be specified."
            );
        }
        if ((type == null) || (typeConverter == null)) {
            throw new IllegalArgumentException(
                    "Type & TypeConverter have to be specified."
            );
        }
        if ((Reflections.isBoolean(type)) || (Optional.class.equals(subClass))) {
            argument = new Optional(names, description, index++, type, typeConverter, constraints, defaultValue);
        }
        else if (Required.class.equals(subClass)) {
            argument = new Required(names, description, index++, type, typeConverter, constraints);
        }
        else if (Positional.class.equals(subClass)) {
            if (arguments.stream().anyMatch(arg -> !(arg instanceof Positional))) {
                throw new IllegalArgumentException(
                        "Positional Arguments have to be positioned at the start."
                );
            }
            argument = new Positional(description, index++, type, typeConverter, constraints, position++);
        }
        else {
            throw new IllegalArgumentException(
                    "This factory instance does not know how to construct instances of subclass: " + subClass + "."
            );
        }

        arguments.add(argument);
        resetVariables();
        return this;
    }

    public ArgumentCollectionFactory append() {
        Argument argument;

        if (namesIsNull(names, subClass)) {
            throw new IllegalArgumentException(
                    "Names has to be specified."
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
                        "The specified TypeConverterMap has no mapping for type: " + type + "."
                );
            }
        }
        if ((Reflections.isBoolean(type)) || (Optional.class.equals(subClass))) {
            argument = new Optional(names, description, index++, type, typeConverter, defaultValue);
        }
        else if (Required.class.equals(subClass)) {
            argument = new Required(names, description, index++, type, typeConverter);
        }
        else if (Positional.class.equals(subClass)) {
            if (arguments.stream().anyMatch(arg -> !(arg instanceof Positional))) {
                throw new IllegalArgumentException(
                        "Positional args if present must be positioned at the start."
                );
            }
            argument = new Positional(description, index++, type, typeConverter, position++);
        }
        else {
            throw new IllegalArgumentException(
                    "This factory instance does not know how to construct instances of subclass: " + subClass + "."
            );
        }

        arguments.add(argument);
        resetVariables();
        return this;
    }

    public List<Argument> get() {
        List<Argument> arguments = new ArrayList<>(this.arguments);
        this.arguments.clear();
        index = 0;
        position = 0;
        resetVariables();
        return arguments;
    }

    private void resetVariables() {
        names = null;
        defaultValue = null;
        description = null;
        subClass = Required.class;
        type = null;
        typeConverter = null;
    }

    private static boolean namesIsNull(final Set<String> names, final Class<? extends Argument> cls) {
        if (cls == Positional.class) {
            return false;
        }
        else {
            return (names == null) || (names.isEmpty());
        }
    }
}