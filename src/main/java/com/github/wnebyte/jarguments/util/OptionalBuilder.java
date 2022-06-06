package com.github.wnebyte.jarguments.util;

import java.util.Set;
import java.util.Collection;
import com.github.wnebyte.jarguments.Constraint;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;

public class OptionalBuilder<T> extends ArgumentBuilder<T> {

    protected String defaultValue = null;

    public OptionalBuilder<T> setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public OptionalBuilder<T> setNames(Set<String> names) {
        super.setNames(names);
        return this;
    }

    @Override
    public OptionalBuilder<T> setDescription(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public OptionalBuilder<T> setMetavar(String metavar) {
        super.setMetavar(metavar);
        return this;
    }

    @Override
    public OptionalBuilder<T> setChoices(Set<String> choices) {
        super.setChoices(choices);
        return this;
    }

    @Override
    public OptionalBuilder<T> setIndex(int index) {
        super.setIndex(index);
        return this;
    }

    @Override
    public OptionalBuilder<T> setType(Class<T> type) {
        super.setType(type);
        return this;
    }

    @Override
    public OptionalBuilder<T> setTypeAdapter(TypeAdapter<T> typeAdapter) {
        super.setTypeAdapter(typeAdapter);
        return this;
    }

    @Override
    public OptionalBuilder<T> setConstraints(Collection<Constraint<T>> constraints) {
        super.setConstraints(constraints);
        return this;
    }

    @Override
    public Optional build() {
        return new Optional(
                names,
                description,
                metavar,
                choices,
                index,
                type,
                typeAdapter,
                constraints,
                defaultValue
        );
    }
}
