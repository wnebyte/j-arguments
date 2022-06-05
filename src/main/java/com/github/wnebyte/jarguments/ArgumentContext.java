package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Iterator;

public class ArgumentContext implements Iterable<Argument> {

    private final String description;

    private final Set<Argument> arguments;

    public ArgumentContext() {
        this(null, null);
    }

    public ArgumentContext(String description, Set<Argument> arguments) {
        super();
        this.description = description;
        this.arguments = arguments;
    }

    public String getDescription() {
        return description;
    }

    public Set<Argument> getArguments() {
        return arguments;
    }

    @Override
    public Iterator<Argument> iterator() {
        return arguments.iterator();
    }
}
