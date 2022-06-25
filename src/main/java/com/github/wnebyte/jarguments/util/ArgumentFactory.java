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
    #      STATIC FIELDS      #
    ###########################
    */

    private static final Collection<Character> DEFAULT_EXCLUDE_CHARACTERS
            = new ArrayList<Character>() {{
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

    private static final Collection<String> DEFAULT_EXCLUDE_NAMES
            = new ArrayList<String>() {{
                add("-h");
                add("--help");
    }};

    /*
    ###########################
    #          FIELDS         #
    ###########################
    */

    private final Collection<Character> excludeCharacters;

    private final Collection<String> excludeNames;

    private final AbstractTypeAdapterRegistry adapters;

    private final Set<String> allNames = new HashSet<>();

    private final List<Argument> c = new ArrayList<>();

    private final Comparator<? super Argument> comparator = Argument::compareTo;

    private int index = 0;

    private int position = 0;

    public ArgumentFactory() {
        this(null, null, null);
    }

    public ArgumentFactory(AbstractTypeAdapterRegistry adapters) {
        this(adapters, null, null);
    }

    public ArgumentFactory(
            AbstractTypeAdapterRegistry adapters,
            Collection<Character> excludeCharacters,
            Collection<String> excludeNames
    ) {
        this.adapters = (adapters == null) ? TypeAdapterRegistry.getInstance() : adapters;
        this.excludeCharacters = new ArrayList<>(DEFAULT_EXCLUDE_CHARACTERS);
        if (excludeCharacters != null) {
            this.excludeCharacters.addAll(excludeCharacters);
        }
        this.excludeNames = (excludeNames == null) ? new ArrayList<>(DEFAULT_EXCLUDE_NAMES) : excludeNames;
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
            typeAdapter = adapters.get(type);

            if (typeAdapter == null) {
                throw new IllegalArgumentException(
                        "No adapter is registered for type: '" + type + "'."
                );
            }
        }

        if (required) {
            if (name == null) {
                // pos
                argument = new PositionalBuilder<T>()
                        .setDescription(description)
                        .setMetavar(metavar)
                        .setChoices(Sets.of(choices))
                        .setIndex(index++)
                        .setType(type)
                        .setTypeAdapter(typeAdapter)
                        .setConstraints(constraints)
                        .setPosition(position++)
                        .build();
            } else {
                // req
                argument = new RequiredBuilder<T>()
                        .setNames(getNames(name))
                        .setDescription(description)
                        .setMetavar(metavar)
                        .setChoices(Sets.of(choices))
                        .setIndex(index++)
                        .setType(type)
                        .setTypeAdapter(typeAdapter)
                        .setConstraints(constraints)
                        .build();
            }
        } else {
            if (name == null) {
                throw new IllegalArgumentException(
                        "A non-positional Argument must have at least one name."
                );
            }
            if (isBoolean(type)) {
                // flag
                argument = new FlagBuilder<T>()
                        .setNames(getNames(name))
                        .setDescription(description)
                        .setMetavar(metavar)
                        .setIndex(index++)
                        .setType(type)
                        .setTypeAdapter(typeAdapter)
                        .setConstraints(constraints)
                        .setValue("true")
                        .setDefaultValue("false")
                        .build();
            } else {
                // opt
                argument = new OptionalBuilder<T>()
                        .setNames(getNames(name))
                        .setDescription(description)
                        .setMetavar(metavar)
                        .setChoices(Sets.of(choices))
                        .setIndex(index++)
                        .setType(type)
                        .setTypeAdapter(typeAdapter)
                        .setConstraints(constraints)
                        .setDefaultValue(defaultValue)
                        .build();
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
                        "An empty Argument name is not allowed."
                );
            }
            if (excludeNames.contains(n)) {
                throw new IllegalArgumentException(
                        String.format(
                                "Argument name: '%s' is not allowed.", n
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

    private void initialize(Argument argument) {
        try {
            if (argument.hasChoices()) {
                for (String choice : argument.getChoices()) {
                    ArgumentSupport.initialize(argument, choice);
                }
            }
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
