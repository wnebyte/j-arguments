package com.github.wnebyte.jarguments.util;

import java.util.Set;
import java.util.Collection;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.Constraint;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;

public abstract class ArgumentBuilder<T> {

    protected Set<String> names = null;

    protected String description = null;

    protected String metavar = null;

    protected Set<String> choices = null;

    protected int index = -1;

    protected Class<T> type = null;

    protected TypeAdapter<T> typeAdapter = null;

    protected Collection<Constraint<T>> constraints = null;

    public ArgumentBuilder<T> setNames(Set<String> names) {
        this.names = names;
        return this;
    }

    public ArgumentBuilder<T> setDescription(String description) {
        this.description = description;
        return this;
    }

    public ArgumentBuilder<T> setMetavar(String metavar) {
        this.metavar = metavar;
        return this;
    }

    public ArgumentBuilder<T> setChoices(Set<String> choices) {
        this.choices = choices;
        return this;
    }

    public ArgumentBuilder<T> setIndex(int index) {
        this.index = index;
        return this;
    }

    public ArgumentBuilder<T> setType(Class<T> type) {
        this.type = type;
        return this;
    }

    public ArgumentBuilder<T> setTypeAdapter(TypeAdapter<T> typeAdapter) {
        this.typeAdapter = typeAdapter;
        return this;
    }

    public ArgumentBuilder<T> setConstraints(Collection<Constraint<T>> constraints) {
        this.constraints = constraints;
        return this;
    }

    public abstract Argument build();
}
