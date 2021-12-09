package com.github.wnebyte.jarguments.factory;

import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.converter.TypeConverter;
import java.util.Collection;
import java.util.List;

public abstract class AbstractArgumentCollectionFactory {

    public abstract Collection<Character> getExcludeCharacters();

    /* --------------- Field Setters */

    public abstract AbstractArgumentCollectionFactory setNames(String... names);

    public abstract AbstractArgumentCollectionFactory setDescription(String desc);

    public abstract AbstractArgumentCollectionFactory setType(Class<?> type);

    public abstract AbstractArgumentCollectionFactory setTypeConverter(TypeConverter<?> converter);

    public abstract AbstractArgumentCollectionFactory setSubClass(Class<? extends Argument> sClass);

    /* --------------- Create Methods --------------- */

    public abstract AbstractArgumentCollectionFactory append();

    public abstract <T> AbstractArgumentCollectionFactory append(Class<T> type);

    public abstract <T> AbstractArgumentCollectionFactory append(Class<T> type, TypeConverter<T> converter);

    public abstract <T> AbstractArgumentCollectionFactory append(Class<T> type, Collection<Constraint<T>> constraints);

    public abstract <T> AbstractArgumentCollectionFactory append(
            Class<T> type, TypeConverter<T> converter, Collection<Constraint<T>> constraints
    );

    public abstract List<Argument> get();

}
