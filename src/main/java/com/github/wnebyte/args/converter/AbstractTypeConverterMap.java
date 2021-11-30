package com.github.wnebyte.args.converter;

import java.util.HashMap;

public abstract class AbstractTypeConverterMap {

    protected HashMap<Class<?>, TypeConverter<?>> converters;

    public abstract <T> void put(Class<T> cls, TypeConverter<T> typeConverter);

    public abstract <T> boolean putIfAbsent(Class<T> cls, TypeConverter<T> typeConverter);

    public abstract <T> TypeConverter<T> get(Class<T> cls);

    public abstract boolean contains(Class<?> cls);
}