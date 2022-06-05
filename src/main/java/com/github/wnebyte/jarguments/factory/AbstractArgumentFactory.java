package com.github.wnebyte.jarguments.factory;

import java.util.List;
import java.util.Collection;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.Constraint;
import com.github.wnebyte.jarguments.adapter.TypeAdapter;

public abstract class AbstractArgumentFactory {

    /*
    ###########################
    #         SETTERS         #
    ###########################
    */

    public abstract AbstractArgumentFactory setName(String... names);

    public abstract AbstractArgumentFactory setDescription(String description);

    public abstract AbstractArgumentFactory setType(Class<?> type);

    public abstract AbstractArgumentFactory setTypeAdapter(TypeAdapter<?> typeAdapter);

    public abstract <T extends Argument> AbstractArgumentFactory setClass(Class<T> cls);

    public abstract AbstractArgumentFactory setDefaultValue(String defaultValue);

    public abstract AbstractArgumentFactory setFlagValue(String flagValue);

    /*
    ###########################
    #         GETTERS         #
    ###########################
    */

    public abstract List<Argument> get();

    public abstract Collection<Character> getExcludedCharacters();

    /*
    ###########################
    #         APPENDERS       #
    ###########################
    */

    public abstract AbstractArgumentFactory append();

    public abstract <T> AbstractArgumentFactory append(Class<T> type);

    public abstract <T> AbstractArgumentFactory append(Class<T> type, TypeAdapter<T> typeAdapter);

    public abstract <T> AbstractArgumentFactory append(Class<T> type, Collection<Constraint<T>> constraints);

    public abstract <T> AbstractArgumentFactory append(
            Class<T> type, TypeAdapter<T> typeAdapter, Collection<Constraint<T>> constraints
    );
}
