package com.github.wnebyte.jarguments.factory;

import java.util.List;
import java.util.Collection;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.Constraint;
import com.github.wnebyte.jarguments.convert.TypeConverter;

public abstract class AbstractArgumentFactory {

    /*
    ###########################
    #         SETTERS         #
    ###########################
    */

    public abstract AbstractArgumentFactory setName(String... names);

    public abstract AbstractArgumentFactory setDescription(String desc);

    public abstract AbstractArgumentFactory setType(Class<?> type);

    public abstract AbstractArgumentFactory setTypeConverter(TypeConverter<?> converter);

    public abstract <T extends Argument> AbstractArgumentFactory setCls(Class<T> sClass);

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

    public abstract <T> AbstractArgumentFactory append(Class<T> type, TypeConverter<T> converter);

    public abstract <T> AbstractArgumentFactory append(Class<T> type, Collection<Constraint<T>> constraints);

    public abstract <T> AbstractArgumentFactory append(
            Class<T> type, TypeConverter<T> converter, Collection<Constraint<T>> constraints
    );
}
