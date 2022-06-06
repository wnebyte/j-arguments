package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Iterator;
import java.util.Collections;
import com.github.wnebyte.jarguments.util.Strings;

public class ArgumentContext implements Iterable<Argument> {

    private final Set<String> names;

    private final String description;

    private final Set<Argument> arguments;

    public ArgumentContext() {
        this(null, null, null);
    }

    public ArgumentContext(Set<String> names, String description, Set<Argument> arguments) {
        this.names = names;
        this.description = description;
        this.arguments = arguments;
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(names);
    }

    public boolean hasNames() {
        return (names != null) && !(names.isEmpty());
    }

    public boolean hasDescription() {
        return (description != null) && !(description.equals(Strings.EMPTY));
    }

    public String getDescription() {
        return description;
    }

    public Set<Argument> getArguments() {
        return Collections.unmodifiableSet(arguments);
    }

    public boolean hasArguments() {
        return (arguments != null) && !(arguments.isEmpty());
    }

    @Override
    public Iterator<Argument> iterator() {
        return arguments.iterator();
    }
}
