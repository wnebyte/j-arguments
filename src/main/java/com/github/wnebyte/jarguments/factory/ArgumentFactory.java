package com.github.wnebyte.jarguments.factory;

import java.util.*;
import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.Constraint;
import com.github.wnebyte.jarguments.adapter.AbstractTypeAdapterRegistry;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;
import com.github.wnebyte.jarguments.adapter.TypeAdapterRegistry;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Reflections;
import com.github.wnebyte.jarguments.util.Strings;

public class ArgumentFactory extends AbstractArgumentFactory {

    /*
    ###########################
    #      STATIC METHODS     #
    ###########################
    */

    /**
     * Returns a new builder instance.
     * @return a new instance.
     */
    public static ArgumentFactoryBuilder builder() {
        return new ArgumentFactoryBuilder();
    }

    private static boolean namesIsNull(Set<String> names, Class<? extends Argument> cls) {
        if (cls == Positional.class) {
            return false;
        }
        else {
            return (names == null || names.isEmpty());
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

    private final AbstractTypeAdapterRegistry typeConverters;

    private final Set<String> allNames = new HashSet<>();

    private final List<Argument> arguments = new ArrayList<>();

    /*
    ###########################
    #        PROPERTIES       #
    ###########################
    */

    private Set<String> names;

    private String description;

    private Class<? extends Argument> cls;

    private Class<?> type;

    private TypeAdapter<?> typeAdapter;

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
     * Constructs a new instance using the specified <code>Collection</code> and <code>AbstractTypeAdapterRegistry</code>.
     * @param exclude chars to be removed from the specified name of each <code>Argument</code>.
     * @param typeConverters to be used.
     */
    public ArgumentFactory(
            Collection<Character> exclude,
            AbstractTypeAdapterRegistry typeConverters
    ) {
        if (exclude != null) {
            this.exclude.addAll(exclude);
        }
        this.typeConverters = (typeConverters != null) ? typeConverters : TypeAdapterRegistry.getInstance();
    }

    /**
     * Constructs a new instance using the specified <code>AbstractTypeAdapterRegistry</code>.
     * @param typeConverters to be used.
     */
    public ArgumentFactory(AbstractTypeAdapterRegistry typeConverters) {
        this(null, typeConverters);
    }

    /**
     * Constructs a new instance using the specified <code>Collection</code>.
     * @param exclude chars to be removed from the specified name of each <code>Argument</code>.
     */
    public ArgumentFactory(Collection<Character> exclude) {
        this(exclude, null);
    }

    /**
     * Constructs a new instance.
     */
    public ArgumentFactory() {
        this(null, null);
    }

    /**
     * Sets the name property for the next <code>Argument</code> to be appended.
     * @param names an array of names.
     * @return this.
     */
    @Override
    public ArgumentFactory setName(String... names) {
        if ((names == null || names.length == 0)) {
            throw new IllegalArgumentException(
                    "Names may not be null/empty."
            );
        }
        this.names = new LinkedHashSet<>(names.length);
        for (String n : names) {
            n = Strings.removeAll(n, exclude);
            if (n.equals(Strings.EMPTY)) {
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
     * Sets the type property for the next <code>Argument</code> to be appended.
     * @param type a type.
     * @return this.
     */
    public ArgumentFactory setType(Class<?> type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the typeAdapter property of the next <code>Argument</code> to be appended.
     * @param typeAdapter a TypeAdapter.
     * @return this.
     */
    public ArgumentFactory setTypeAdapter(TypeAdapter<?> typeAdapter) {
        this.typeAdapter = typeAdapter;
        return this;
    }

    /**
     * Sets the description property for the next <code>Argument</code> to be appended.
     * @param description a description.
     * @return this.
     */
    public ArgumentFactory setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the subclass for the next <code>Argument</code> to be appended.
     * @param cls a subclass of Argument.
     * @return this.
     */
    public <T extends Argument> ArgumentFactory setClass(Class<T> cls) {
        this.cls = cls;
        return this;
    }

    /**
     * Sets the defaultValue property for the next <code>Argument</code> to be appended.
     * @param defaultValue a default value.
     * @return this.
     */
    public ArgumentFactory setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Sets the flagValue property for the next <code>Argument</code> to be appended.
     * @param flagValue a flag value.
     * @return this.
     */
    public ArgumentFactory setFlagValue(String flagValue) {
        this.flagValue = flagValue;
        return this;
    }

    /**
     * Specifies that the subclass of the next <code>Argument</code> to be appended is {@link Required}.
     * @return this.
     */
    public ArgumentFactory isRequired() {
        this.cls = Required.class;
        return this;
    }

    /**
     * Specifies that the subclass of the next <code>Argument</code> to be appended is {@link Optional}.
     * @return this.
     */
    public ArgumentFactory isOptional() {
        this.cls = Optional.class;
        return this;
    }

    /**
     * Specifies that the subclass of the next <code>Argument</code> to be appended is {@link Positional}.
     * @return this.
     */
    public ArgumentFactory isPositional() {
        this.cls = Positional.class;
        return this;
    }

    /**
     * Specifies that the subclass of the next <code>Argument</code> to be appended is {@link Flag}.
     * @return this.
     */
    public ArgumentFactory isFlag() {
        this.cls = Flag.class;
        return this;
    }

    /**
     * Constructs a new <code>Argument</code> using the specified <code>Class</code>.
     * @param type a type;
     * @return this.
     */
    public <T> ArgumentFactory append(Class<T> type) {
        TypeAdapter<T> typeAdapter = typeConverters.get(type);
        return append(type, typeAdapter);
    }

    /**
     * Constructs a new <code>Argument</code> using the specified <code>Class</code> and
     * <code>TypeAdapter</code>.
     * @param type a type.
     * @param typeAdapter a TypeAdapter.
     * @return this.
     */
    public <T> ArgumentFactory append(Class<T> type, TypeAdapter<T> typeAdapter) {
        return append(type, typeAdapter, null);
    }

    /**
     * Constructs a new <code>Argument</code> using preset values and the specified <code>type</code> and
     * <code>Collection</code>.
     * @param type a type.
     * @param constraints a Collection.
     * @return this.
     */
    public <T> ArgumentFactory append(Class<T> type, Collection<Constraint<T>> constraints) {
        TypeAdapter<T> typeAdapter = typeConverters.get(type);
        return append(type, typeAdapter, constraints);
    }

    /**
     * Constructs a new <code>Argument</code> using the specified <code>Class</code>,
     * <code>TypeAdapter</code> and <code>Collection</code>.
     * @param type a type.
     * @param typeAdapter a TypeAdapter.
     * @param constraints a Collection.
     * @return this.
     */
    public <T> ArgumentFactory append(
            Class<T> type,
            TypeAdapter<T> typeAdapter,
            Collection<Constraint<T>> constraints

    ) {
        final Argument argument = null;

        if (type == null || typeAdapter == null) {
            throw new IllegalArgumentException(
                    "Type & TypeAdapter have to be non-null."
            );
        }
        if (cls == null) {
            cls = Reflections.isBoolean(type) ? Flag.class : Required.class;
        }
        if (namesIsNull(names, cls)) {
            throw new IllegalArgumentException(
                    "At least one name has to be specified for each non-positional Argument."
            );
        }
        /*
        if (Reflections.isBoolean(type) && cls == Flag.class) {
            argument = new Flag(
                    names,
                    description,
                    null,
                    index++,
                    type,
                    typeAdapter,
                    flagValue == null ? "true" : flagValue,
                    defaultValue
            );
        }
        else if (Flag.class.equals(cls)) {
            if (flagValue == null) {
                throw new IllegalArgumentException(
                        "FlagValue has to be specified for instances of Flag if type is not of type Boolean."
                );
            }
            argument = new Flag(
                    names,
                    description,
                    null,
                    index++,
                    type,
                    typeAdapter,
                    flagValue,
                    defaultValue
            );
        }
        else if (Optional.class.equals(cls)) {
            argument = new Optional(
                    names,
                    description,
                    null,
                    null,
                    index++,
                    type,
                    typeAdapter,
                    constraints,
                    defaultValue
            );
        }
        else if (Required.class.equals(cls)) {
            argument = new Required(
                    names,
                    description,
                    null,
                    null,
                    index++,
                    type,
                    typeAdapter,
                    constraints
            );
        }
        else if (Positional.class.equals(cls)) {
            argument = new Positional(
                    description,
                    null,
                    null,
                    index++,
                    type,
                    typeAdapter,
                    constraints,
                    position++
            );
        }
        else {
            throw new IllegalArgumentException(
                    String.format(
                            "This factory instance does not know how to construct instances of class: %s.", cls
                    )
            );
        }
         */

        dryrun(argument);
        arguments.add(argument);
        reset();
        return this;
    }

    /**
     * Constructs a new <code>Argument</code>.
     */
    public ArgumentFactory append() {
        final Argument argument = null;

        if (namesIsNull(names, cls)) {
            throw new IllegalArgumentException(
                    "At least one name has to be specified for each non-positional Argument."
            );
        }
        if (type == null) {
            throw new IllegalArgumentException(
                    "Type has to be specified."
            );
        }
        if (typeAdapter == null) {
            typeAdapter = typeConverters.get(type);

            if (typeAdapter == null) {
                throw new IllegalArgumentException(
                        String.format(
                                "This factory instance's TypeAdapterRegistry has no mapping for Type: %s.", type
                        )
                );
            }
        }
        if (cls == null) {
            cls = Reflections.isBoolean(type) ? Flag.class : Required.class;
        }
        /*
        if (Reflections.isBoolean(type) && cls == Flag.class) {
            argument = new Flag(
                    names,
                    description,
                    null,
                    index++,
                    type,
                    typeAdapter,
                    flagValue == null ? "true" : flagValue,
                    defaultValue
            );
        }
        else if (Flag.class.equals(cls)) {
            if (flagValue == null) {
                throw new IllegalArgumentException(
                        "FlagValue has to be specified for instances of Flag if type is not of type Boolean."
                );
            }
            argument = new Flag(
                    names,
                    description,
                    null,
                    index++,
                    type,
                    typeAdapter,
                    flagValue,
                    defaultValue
            );
        }
        else if (Optional.class.equals(cls)) {
            argument = new Optional(
                    names,
                    description,
                    null,
                    null,
                    index++,
                    type,
                    typeAdapter,
                    defaultValue
            );
        }
        else if (Required.class.equals(cls)) {
            argument = new Required(
                    names,
                    description,
                    null,
                    null,
                    index++,
                    type,
                    typeAdapter
            );
        }
        else if (Positional.class.equals(cls)) {
            argument = new Positional(
                    description,
                    null,
                    null,
                    index++,
                    type,
                    typeAdapter,
                    position++
            );
        }
        else {
            throw new IllegalArgumentException(
                    String.format(
                            "This factory instance does not know how to construct instances of class: %s.", cls
                    )
            );
        }
         */
        dryrun(argument);
        arguments.add(argument);
        reset();
        return this;
    }

    @Override
    public Collection<Character> getExcludedCharacters() {
        return Collections.unmodifiableCollection(exclude);
    }

    private void dryrun(Argument a) {
        if (a instanceof Optional) {
            try {
                ArgumentSupport.initialize(a, Strings.EMPTY);
            } catch (ParseException e) {
                throw new IllegalArgumentException(
                        e.getMessage()
                );
            }
        }
        if (a instanceof Flag) {
            try {
                ArgumentSupport.initialize(a, a.getNames().toArray(new String[0])[0]);
            } catch (ParseException e) {
                throw new IllegalArgumentException(
                        e.getMessage()
                );
            }
        }
    }

    private void reset() {
        names = null;
        defaultValue = null;
        flagValue = null;
        description = null;
        cls = null;
        type = null;
        typeAdapter = null;
    }

    /**
     * Returns the sorted <code>Collection</code> of Arguments.
     * @return the Arguments.
     */
    public List<Argument> get() {
        List<Argument> arguments = new ArrayList<>(this.arguments);
        this.arguments.clear();
        index = 0;
        position = 0;
        reset();
        allNames.clear();
        arguments.sort(Argument::compareTo);
        return arguments;
    }
}