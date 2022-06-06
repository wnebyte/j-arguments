package com.github.wnebyte.jarguments.util;

import java.util.Set;
import java.util.Collection;
import com.github.wnebyte.jarguments.Constraint;
import com.github.wnebyte.jarguments.Flag;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;

public class FlagBuilder<T> extends OptionalBuilder<T> {

    protected String value = null;

    public FlagBuilder<T> setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public FlagBuilder<T> setNames(Set<String> names) {
        super.setNames(names);
        return this;
    }

    @Override
    public FlagBuilder<T> setDescription(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public FlagBuilder<T> setMetavar(String metavar) {
        super.setMetavar(metavar);
        return this;
    }

    @Override
    public FlagBuilder<T> setChoices(Set<String> choices) {
        super.setChoices(choices);
        return this;
    }

    @Override
    public FlagBuilder<T> setIndex(int index) {
        super.setIndex(index);
        return this;
    }

    @Override
    public FlagBuilder<T> setType(Class<T> type) {
        super.setType(type);
        return this;
    }

    @Override
    public FlagBuilder<T> setTypeAdapter(TypeAdapter<T> typeAdapter) {
        super.setTypeAdapter(typeAdapter);
        return this;
    }

    @Override
    public FlagBuilder<T> setConstraints(Collection<Constraint<T>> constraints) {
        super.setConstraints(constraints);
        return this;
    }

    @Override
    public Flag build() {
        return new Flag(
                names,
                description,
                metavar,
                index,
                type,
                typeAdapter,
                constraints,
                value,
                defaultValue
        );
    }
}
