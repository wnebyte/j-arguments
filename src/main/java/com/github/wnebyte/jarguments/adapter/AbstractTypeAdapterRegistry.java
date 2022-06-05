package com.github.wnebyte.jarguments.adapter;

import java.util.HashMap;

public abstract class AbstractTypeAdapterRegistry {

    protected HashMap<Class<?>, TypeAdapter<?>> converters;

    public abstract <T> void register(Class<T> cls, TypeAdapter<T> typeAdapter);

    public abstract <T> boolean registerIfAbsent(Class<T> cls, TypeAdapter<T> typeAdapter);

    public abstract <T> TypeAdapter<T> get(Class<T> cls);

    public abstract boolean contains(Class<?> cls);
}