package com.github.wnebyte.jarguments.util;

import java.util.Set;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.ArgumentContext;

public class ArgumentContextBuilder {

    private String description = null;

    private Set<Argument> arguments = null;

    public ArgumentContextBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ArgumentContextBuilder setArguments(Set<Argument> arguments) {
        this.arguments = arguments;
        return this;
    }

    public ArgumentContext build() {
        return null;
    }
}
