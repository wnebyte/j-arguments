package com.github.wnebyte.jarguments.util;

import java.util.*;
import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;
import com.github.wnebyte.jarguments.adapter.TypeAdapterRegistry;
import com.github.wnebyte.jarguments.adapter.AbstractTypeAdapterRegistry;
import com.github.wnebyte.jarguments.exception.ParseException;

import static com.github.wnebyte.jarguments.util.Reflections.isBoolean;

public class ArgumentFactory implements AbstractArgumentFactory {

    /*
    ###########################
    #          FIELDS         #
    ###########################
    */

    private final Collection<Character> excludeCharacters = new ArrayList<Character>() {{
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

    private final Collection<String> excludeNames = new ArrayList<String>() {{
        add("-h");
        add("--help");
    }};

    private final AbstractTypeAdapterRegistry typeAdapters;

    private final Set<String> allNames = new HashSet<>();

    private int index = 0;

    private int position = 0;

    private final List<Argument> c = new ArrayList<>();

    private final Comparator<? super Argument> comparator = Argument::compareTo;

    public ArgumentFactory() {
        this(null, null, null);
    }

    public ArgumentFactory(
            AbstractTypeAdapterRegistry typeAdapters,
            Collection<Character> excludeCharacters,
            Collection<String> excludeNames
    ) {
        this.typeAdapters = (typeAdapters == null) ? TypeAdapterRegistry.getInstance() : typeAdapters;
        if (excludeCharacters != null) {
            this.excludeCharacters.addAll(excludeCharacters);
        }
        if (excludeNames != null) {
            this.excludeNames.addAll(excludeNames);
        }
    }

    private Set<String> getNames(String name) {
        // check input
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(
                    "Name must not be null or empty."
            );
        }
        String[] tmp = name.split(",");
        Set<String> names = new LinkedHashSet<>(tmp.length);
        for (String n : tmp) {
            n = Strings.removeAll(n, excludeCharacters);
            if (n.equals(Strings.EMPTY)) {
                throw new IllegalArgumentException(
                        String.format(
                                "An Argument's name is not allowed to be empty after normalization. " +
                                        "Are removed: %s", Arrays.toString(excludeCharacters.toArray())
                        )
                );
            }
            if (excludeNames.contains(n)) {
                throw new IllegalArgumentException(
                        String.format(
                                "An Argument's name is not allowed to be: '%s' .", n
                        )
                );
            }
            boolean distinct = names.add(n) & allNames.add(n);
            if (!distinct) {
                throw new IllegalArgumentException(
                        String.format(
                                "An Argument with name: '%s' has already been created by this factory instance.", n
                        )
                );
            }
        }

        return names;
    }

    @Override
    public Argument create(
            String name,
            String description,
            boolean required,
            String[] choices,
            String metavar,
            String defaultValue,
            Class<?> type
    ) {
        return create(name, description, required, choices, metavar, defaultValue, type, null, null);
    }

    @Override
    public <T> Argument create(
            String name,
            String description,
            boolean required,
            String[] choices,
            String metavar,
            String defaultValue,
            Class<T> type,
            Collection<Constraint<T>> constraints
    ) {
        return create(name, description, required, choices, metavar, defaultValue, type, null, constraints);
    }

    @Override
    public <T> Argument create(
            String name,
            String description,
            boolean required,
            String[] choices,
            String metavar,
            String defaultValue,
            Class<T> type,
            TypeAdapter<T> typeAdapter,
            Collection<Constraint<T>> constraints
    ) {
        final Argument argument;

        // check input
        if (type == null) {
            throw new IllegalArgumentException(
                    "Type must not be null."
            );
        }
        if (typeAdapter == null) {
            typeAdapter = typeAdapters.get(type);

            if (typeAdapter == null) {
                throw new IllegalArgumentException(
                        "No mapping exists for type: '" + type + "'."
                );
            }
        }

        if (required) {
            if (name == null) {
                // pos
                argument = new Positional(
                        description,
                        metavar,
                        Sets.of(choices),
                        index++,
                        type,
                        typeAdapter,
                        constraints,
                        position++
                );
            } else {
                // req
                argument = new Required(
                        getNames(name),
                        description,
                        metavar,
                        Sets.of(choices),
                        index++,
                        type,
                        typeAdapter,
                        constraints
                );
            }
        } else {
            if (name == null) {
                throw new IllegalArgumentException(
                        "A non-positional Argument must have at least one name."
                );
            }
            if (isBoolean(type)) {
                // flag
                argument = new Flag(
                        getNames(name),
                        description,
                        metavar,
                        index++,
                        type,
                        typeAdapter,
                        constraints,
                        "true",
                        defaultValue
                );
            } else {
                // opt
                argument = new Optional(
                        getNames(name),
                        description,
                        metavar,
                        Sets.of(choices),
                        index++,
                        type,
                        typeAdapter,
                        constraints,
                        defaultValue
                );
            }
        }

        initialize(argument);
        c.add(argument);
        return argument;
    }

    @Override
    public Set<Argument> getAll() {
        c.sort(comparator);
        return new LinkedHashSet<>(c);
    }

    private void initialize(Argument argument) {
        try {
            if (argument instanceof Flag) {
                ArgumentSupport.initialize(argument, argument.getCanonicalName());
            } else if (argument instanceof Optional) {
                ArgumentSupport.initialize(argument, Strings.EMPTY);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getCause());
        }
    }
}
