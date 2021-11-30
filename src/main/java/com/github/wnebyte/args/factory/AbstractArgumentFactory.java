package com.github.wnebyte.args.factory;

import com.github.wnebyte.args.Argument;
import com.github.wnebyte.args.constraint.Constraint;
import com.github.wnebyte.args.converter.TypeConverter;
import java.util.Collection;
import java.util.List;

public abstract class AbstractArgumentFactory {

    /* --------------- Field Setters */

    public abstract AbstractArgumentFactory setNames(String... names);

    public abstract AbstractArgumentFactory setDescription(String desc);

    public abstract AbstractArgumentFactory setType(Class<?> cls);

    public abstract AbstractArgumentFactory setTypeConverter(TypeConverter<?> converter);

    public abstract AbstractArgumentFactory setSubClass(Class<? extends Argument> subClass);

    /* --------------- Create Methods --------------- */

    public abstract AbstractArgumentFactory create();

    public abstract <T> AbstractArgumentFactory create(Class<T> type);

    public abstract <T> AbstractArgumentFactory create(Class<T> type, TypeConverter<T> converter);

    public abstract <T> AbstractArgumentFactory create(Class<T> type, Collection<Constraint<T>> constraints);

    public abstract <T> AbstractArgumentFactory create(
            Class<T> type,
            TypeConverter<T> converter,
            Collection<Constraint<T>> constraints
    );

    public abstract List<Argument> getArguments();
}
