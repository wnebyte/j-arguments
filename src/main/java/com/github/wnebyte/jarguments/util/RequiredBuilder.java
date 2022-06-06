package com.github.wnebyte.jarguments.util;

import java.util.Set;
import java.util.Collection;
import com.github.wnebyte.jarguments.Constraint;
import com.github.wnebyte.jarguments.Required;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;

public class RequiredBuilder<T> extends ArgumentBuilder<T> {

    @Override
    public RequiredBuilder<T> setNames(Set<String> names) {
        super.setNames(names);
        return this;
    }

    @Override
    public RequiredBuilder<T> setDescription(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public RequiredBuilder<T> setMetavar(String metavar) {
        super.setMetavar(metavar);
        return this;
    }

    @Override
    public RequiredBuilder<T> setChoices(Set<String> choices) {
        super.setChoices(choices);
        return this;
    }

    @Override
    public RequiredBuilder<T> setIndex(int index) {
        super.setIndex(index);
        return this;
    }

    @Override
    public RequiredBuilder<T> setType(Class<T> type) {
        super.setType(type);
        return this;
    }

    @Override
    public RequiredBuilder<T> setTypeAdapter(TypeAdapter<T> typeAdapter) {
        super.setTypeAdapter(typeAdapter);
        return this;
    }

    @Override
    public RequiredBuilder<T> setConstraints(Collection<Constraint<T>> constraints) {
        super.setConstraints(constraints);
        return this;
    }

    @Override
    public Required build() {
        return new Required(
                names,
                description,
                metavar,
                choices,
                index,
                type,
                typeAdapter,
                constraints
        );
    }
}
