package com.github.wnebyte.jarguments.util;

import java.util.Set;
import java.util.Collection;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.Constraint;
import com.github.wnebyte.jarguments.adapter.AbstractTypeAdapterRegistry;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;

public interface AbstractArgumentFactory {

    Argument create(
            String name,
            String description,
            boolean required,
            String[] choices,
            String metavar,
            String defaultValue,
            Class<?> type
    );

    <T> Argument create(
            String name,
            String description,
            boolean required,
            String[] choices,
            String metavar,
            String defaultValue,
            Class<T> type,
            Collection<Constraint<T>> constraints
    );

    <T> Argument create(
            String name,
            String description,
            boolean required,
            String[] choices,
            String metavar,
            String defaultValue,
            Class<T> type,
            TypeAdapter<T> typeAdapter,
            Collection<Constraint<T>> constraints
    );

    Set<Argument> getAll();

    AbstractTypeAdapterRegistry getTypeAdapters();

    Collection<Character> getExcludeCharacters();

    Collection<String> getExcludeNames();
}
