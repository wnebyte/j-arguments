package com.github.wnebyte.args;

import com.github.wnebyte.args.util.Reflections;
import com.github.wnebyte.args.util.Strings;
import java.util.*;

/**
 * This class declares methods for building multiple instances of subclasses of the {@link Argument} class.
 */
public class ArgumentFactory {

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

    private final AbstractTypeConverters typeConverters;

    private final Set<String> allNames = new HashSet<>();

    private final List<Argument> arguments = new ArrayList<>();

    /*
    ###########################
    #   ARGUMENT PROPERTIES   #
    ###########################
    */
    private Set<String> names;

    private String description;

    private Class<? extends Argument> cls;

    private Class<?> type;

    private TypeConverter<?> typeConverter;

    private int index = 0;

    private int position = 0;

    private String defaultValue;

    /**
     * Constructs a new instance using the specified <code>exclude</code> and <code>typeConverters</code>.
     * @param exclude elements are added to an internal collection, contents will be used to exclude characters
     * from being present in the name(s) of constructed argument(s).
     * @param typeConverters will be used when constructing new arguments if no individual {@linkplain TypeConverter}
     * has been set.
     */
    public ArgumentFactory(
            final Collection<Character> exclude,
            final AbstractTypeConverters typeConverters
    ) {
        if (exclude != null) {
            this.exclude.addAll(exclude);
        }
        this.typeConverters = (typeConverters != null) ?
                typeConverters : TypeConverters.getInstance();
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
                        "All names have to be distinct across all arguments created using this instance."
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
     * Sets the defaultValue property of the next argument to be created.
     * @param defaultValue of the next (optional) argument.
     * @return this.
     */
    public ArgumentFactory setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
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

    /**
     * Specifies that the next argument to be created is to be a required argument.
     * @return this.
     */
    public ArgumentFactory setRequired() {
        this.cls = Required.class;
        return this;
    }

    /**
     * Specifies that the next argument to be created is to be an optional argument.
     * @return this.
     */
    public ArgumentFactory setOptional() {
        this.cls = Optional.class;
        return this;
    }

    /**
     * Specifies that the next argument to be created is to be a positional argument.
     * @return this.
     */
    public ArgumentFactory setPositional() {
        this.cls = Positional.class;
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
        if ((namesIsNull(names, cls)) || (type == null) || (typeConverter == null)) {
            throw new IllegalStateException(
                    "Names, Type & TypeConverter has to be set."
            );
        }
        Argument argument;

        if ((Reflections.isBoolean(type)) || (Optional.class.equals(cls))) {
            argument = new Optional(names, description, index++, type, typeConverter, constraints, defaultValue);
            arguments.add(argument);
        }
        else if (Required.class.equals(cls)) {
            argument = new Required(names, description, index++, type, typeConverter, constraints);
            arguments.add(argument);
        }
        else if (Positional.class.equals(cls)) {
            if (arguments.stream().anyMatch(arg -> !(arg instanceof Positional))) {
                throw new IllegalStateException(
                        "Positional args if present must be positioned at the start."
                );
            }
            argument = new Positional(description, index++, type, typeConverter, constraints, position++);
            arguments.add(argument);
        }
        reset();
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
        if ((namesIsNull(names, cls)) || (type == null) || (typeConverter == null)) {
            throw new IllegalStateException(
                    "Names, Type & TypeConverter has to be set."
            );
        }
        Argument argument;

        if ((Reflections.isBoolean(type)) || (Optional.class.equals(cls))) {
            argument = new Optional(names, description, index++, type, typeConverter, defaultValue);
            arguments.add(argument);
        }
        else if (Required.class.equals(cls)) {
            argument = new Required(names, description, index++, type, typeConverter);
            arguments.add(argument);
        }
        else if (Positional.class.equals(cls)) {
            if (ArgumentUtil.containsSubclasses(arguments, Arrays.asList(Required.class, Optional.class))) {
                throw new IllegalStateException(
                        "Positional args if present must be positioned at the start."
                );
            }
            argument = new Positional(description, index++, type, typeConverter, position++);
            arguments.add(argument);
        }
        reset();
        return this;
    }

    private void reset() {
        names = null;
        defaultValue = null;
        description = null;
        cls = Required.class;
    }

    private boolean namesIsNull(final Set<String> names, final Class<? extends Argument> cls) {
        if (cls == Positional.class) {
            return false;
        }
        else {
            return (names == null) || (names.isEmpty());
        }
    }

    public List<Argument> getArguments() {
        List<Argument> arguments = new ArrayList<>(this.arguments);
        this.arguments.clear();
        index = 0;
        position = 0;
        return arguments;
    }
}