package com.github.wnebyte.args.factory;

import com.github.wnebyte.args.*;
import com.github.wnebyte.args.Optional;
import com.github.wnebyte.args.constraint.Constraint;
import com.github.wnebyte.args.converter.AbstractTypeConverterMap;
import com.github.wnebyte.args.converter.TypeConverter;
import com.github.wnebyte.args.converter.TypeConverterMap;
import com.github.wnebyte.args.util.Reflections;
import com.github.wnebyte.args.util.Strings;

import java.util.*;

/**
 * This class declares methods for building multiple instances of subclasses of the {@link Argument} class.
 */
public class ArgumentFactory extends AbstractArgumentFactory {

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
    public ArgumentFactory(
            Collection<Character> exclude,
            AbstractTypeConverterMap typeConverters
    ) {
        this.exclude.addAll(exclude);
        this.typeConverters = (typeConverters != null) ?
                typeConverters : TypeConverterMap.getInstance();
    }

    /**
     * Sets the name property of the next argument to be created.
     * @param names of the next argument.
     * @return this.
     */
    public ArgumentFactory setNames(final String... names) {
        if ((names == null) || (names.length == 0)) {
            throw new IllegalArgumentException(
                    "Names may not be null/empty."
            );
        }
        this.names = new HashSet<>(names.length);
        for (String n : names) {
            n = Strings.exclude(n, exclude);
            boolean success = this.names.add(n) & allNames.add(n);
            if (!success) {
                throw new IllegalArgumentException(
                        "All names have to be distinct across all of the arguments created using this instance."
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
    public ArgumentFactory setType(final Class<?> type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the typeConverter property of the next argument to be created.
     * @param typeConverter of the next argument.
     * @return this.
     */
    public ArgumentFactory setTypeConverter(final TypeConverter<?> typeConverter) {
        this.typeConverter = typeConverter;
        return this;
    }

    /**
     * Sets the description property of the next argument to be created.
     * @param description of the next argument.
     * @return this.
     */
    public ArgumentFactory setDescription(final String description) {
        this.description = description;
        return this;
    }

    public ArgumentFactory setSubClass(final Class<? extends Argument> subClass) {
        this.subClass = subClass;
        return this;
    }

    /**
     * Sets the defaultValue property of the next argument to be created.
     * @param defaultValue of the next (optional) argument.
     * @return this.
     */
    public ArgumentFactory setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Specifies that the next argument to be created is to be a required argument.
     * @return this.
     */
    public ArgumentFactory isRequired() {
        this.subClass = Required.class;
        return this;
    }

    /**
     * Specifies that the next argument to be created is to be an optional argument.
     * @return this.
     */
    public ArgumentFactory isOptional() {
        this.subClass = com.github.wnebyte.args.Optional.class;
        return this;
    }

    /**
     * Specifies that the next argument to be created is to be a positional argument.
     * @return this.
     */
    public ArgumentFactory isPositional() {
        this.subClass = Positional.class;
        return this;
    }

    /**
     * Creates a new argument using the specified <code>type</code>.
     * @return this.
     */
    public <T> ArgumentFactory create(final Class<T> type) {
        TypeConverter<T> typeConverter = typeConverters.get(type);
        return create(type, typeConverter);
    }
    /**
     * Creates a new argument using the specified <code>type</code> and <code>typeConverter</code>.
     * @return this.
     */
    public <T> ArgumentFactory create(final Class<T> type, final TypeConverter<T> typeConverter) {
        return create(type, typeConverter, null);
    }

    public <T> ArgumentFactory create(final Class<T> type, final Collection<Constraint<T>> constraints) {
        TypeConverter<T> typeConverter = typeConverters.get(type);
        return create(type, typeConverter, constraints);
    }

    public <T> ArgumentFactory create(
            final Class<T> type,
            final TypeConverter<T> typeConverter,
            final Collection<Constraint<T>> constraints

    ) {
        if ((namesIsNull(names, subClass)) || (type == null) || (typeConverter == null)) {
            throw new IllegalStateException(
                    "Names, Type & TypeConverter has to be set."
            );
        }
        Argument argument;

        if ((Reflections.isBoolean(type)) || (com.github.wnebyte.args.Optional.class.equals(subClass))) {
            argument = new com.github.wnebyte.args.Optional(names, description, index++, type, typeConverter, constraints, defaultValue);
            arguments.add(argument);
        }
        else if (Required.class.equals(subClass)) {
            argument = new Required(names, description, index++, type, typeConverter, constraints);
            arguments.add(argument);
        }
        else if (Positional.class.equals(subClass)) {
            if (arguments.stream().anyMatch(arg -> !(arg instanceof Positional))) {
                throw new IllegalStateException(
                        "Positional args if present must be positioned at the start."
                );
            }
            argument = new Positional(description, index++, type, typeConverter, constraints, position++);
            arguments.add(argument);
        }
        resetOptionalVars();
        return this;
    }

    public ArgumentFactory create() {
        if (type == null) {
            throw new IllegalStateException(
                    "Type must not be null."
            );
        }
        if (typeConverter == null) {
            typeConverter = typeConverters.get(type);
        }
        if ((namesIsNull(names, subClass)) || (type == null) || (typeConverter == null)) {
            throw new IllegalStateException(
                    "Names, Type & TypeConverter has to be set."
            );
        }
        Argument argument;

        if ((Reflections.isBoolean(type)) || (com.github.wnebyte.args.Optional.class.equals(subClass))) {
            argument = new com.github.wnebyte.args.Optional(names, description, index++, type, typeConverter, defaultValue);
            arguments.add(argument);
        }
        else if (Required.class.equals(subClass)) {
            argument = new Required(names, description, index++, type, typeConverter);
            arguments.add(argument);
        }
        else if (Positional.class.equals(subClass)) {
            if (ArgumentSupport.containsInstancesOfSubClasses(arguments, Arrays.asList(Required.class, Optional.class))) {
                throw new IllegalStateException(
                        "Positional args if present must be positioned at the start."
                );
            }
            argument = new Positional(description, index++, type, typeConverter, position++);
            arguments.add(argument);
        }
        resetOptionalVars();
        return this;
    }

    public List<Argument> getArguments() {
        List<Argument> arguments = new ArrayList<>(this.arguments);
        this.arguments.clear();
        index = 0;
        position = 0;
        return arguments;
    }

    private void resetOptionalVars() {
        names = null;
        defaultValue = null;
        description = null;
        subClass = Required.class;
    }

    private boolean namesIsNull(final Set<String> names, final Class<? extends Argument> cls) {
        if (cls == Positional.class) {
            return false;
        }
        else {
            return (names == null) || (names.isEmpty());
        }
    }
}