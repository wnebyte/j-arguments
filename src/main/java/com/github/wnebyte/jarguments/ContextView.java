package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Objects;
import java.util.Collections;
import com.github.wnebyte.jarguments.util.Strings;

public class ContextView {

    public static ContextView of(Set<Argument> c) {
        return new ContextView(null, null, c);
    }

    public static ContextView of(String description, Set<Argument> c) {
        return new ContextView(null, description, c);
    }

    public static ContextView of(String name, String description, Set<Argument> c) {
        return new ContextView(name, description, c);
    }

    protected final String name;

    protected final String description;

    protected final Set<Argument> arguments;

    public ContextView(String name, String description, Set<Argument> arguments) {
        this.name = name;
        this.description = description;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        return (name != null) && !(name.equals(Strings.EMPTY));
    }

    public String getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return (description != null) && !(description.equals(Strings.EMPTY));
    }

    public Set<Argument> getArguments() {
        return Collections.unmodifiableSet(arguments);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof ContextView)) return false;
        ContextView context = (ContextView) o;
        return Objects.equals(context.name, this.name) &&
                Objects.equals(context.description, this.description) &&
                Objects.equals(context.arguments, this.arguments);
    }

    @Override
    public int hashCode() {
        int result = 33;
        return 2 + result +
                Objects.hashCode(name) +
                Objects.hashCode(description) +
                Objects.hashCode(arguments);
    }

    @Override
    public String toString() {
        return String.format(
                "ContextView(name: %s, description: %s, arguments: %s)",
                name, description, arguments
        );
    }
}
