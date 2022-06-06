package com.github.wnebyte.jarguments.adapter;

import java.util.HashMap;

public abstract class AbstractTypeAdapterRegistry {

    protected HashMap<Class<?>, TypeAdapter<?>> adapters;

    public abstract <T> void register(Class<T> type, TypeAdapter<T> typeAdapter);

    public abstract <T> boolean registerIfAbsent(Class<T> type, TypeAdapter<T> typeAdapter);

    public abstract <T> TypeAdapter<T> get(Class<T> type);

    public abstract boolean isRegistered(Class<?> type);
}