package com.github.wnebyte.jarguments.util;

import java.util.Set;
import java.util.Collection;
import com.github.wnebyte.jarguments.Constraint;
import com.github.wnebyte.jarguments.Positional;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;

public class PositionalBuilder<T> extends ArgumentBuilder<T> {

    protected int position = -1;

    public PositionalBuilder<T> setPosition(int position) {
        this.position = position;
        return this;
    }

    @Override
    public PositionalBuilder<T> setNames(Set<String> names) {
        super.setNames(names);
        return this;
    }

    @Override
    public PositionalBuilder<T> setDescription(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public PositionalBuilder<T> setMetavar(String metavar) {
        super.setMetavar(metavar);
        return this;
    }

    @Override
    public PositionalBuilder<T> setChoices(Set<String> choices) {
        super.setChoices(choices);
        return this;
    }

    @Override
    public PositionalBuilder<T> setIndex(int index) {
        super.setIndex(index);
        return this;
    }

    @Override
    public PositionalBuilder<T> setType(Class<T> type) {
        super.setType(type);
        return this;
    }

    @Override
    public PositionalBuilder<T> setTypeAdapter(TypeAdapter<T> typeAdapter) {
        super.setTypeAdapter(typeAdapter);
        return this;
    }

    @Override
    public PositionalBuilder<T> setConstraints(Collection<Constraint<T>> constraints) {
        super.setConstraints(constraints);
        return this;
    }

    @Override
    public Positional build() {
        return new Positional(
                description,
                metavar,
                choices,
                index,
                type,
                typeAdapter,
                constraints,
                position
        );
    }
}
