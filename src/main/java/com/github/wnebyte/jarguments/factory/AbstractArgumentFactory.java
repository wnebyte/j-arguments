package com.github.wnebyte.jarguments.factory;

import java.util.Collection;
import java.util.List;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.convert.TypeConverter;

public abstract class AbstractArgumentFactory {

    public abstract Collection<Character> getExcludeCharacters();

    /* --------------- Field Setters */

    public abstract AbstractArgumentFactory setName(String... names);

    public abstract AbstractArgumentFactory setDescription(String desc);

    public abstract AbstractArgumentFactory setType(Class<?> type);

    public abstract AbstractArgumentFactory setTypeConverter(TypeConverter<?> converter);

    public abstract AbstractArgumentFactory setSubClass(Class<? extends Argument> sClass);

    public abstract AbstractArgumentFactory setDefaultValue(String defaultValue);

    public abstract AbstractArgumentFactory setFlagValue(String flagValue);

    /* --------------- Create Methods --------------- */

    public abstract AbstractArgumentFactory append();

    public abstract <T> AbstractArgumentFactory append(Class<T> type);

    public abstract <T> AbstractArgumentFactory append(Class<T> type, TypeConverter<T> converter);

    public abstract <T> AbstractArgumentFactory append(Class<T> type, Collection<Constraint<T>> constraints);

    public abstract <T> AbstractArgumentFactory append(
            Class<T> type, TypeConverter<T> converter, Collection<Constraint<T>> constraints
    );

    public abstract List<Argument> get();

}
