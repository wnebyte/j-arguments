package com.github.wnebyte.args;

/**
 * This interface declares some of the same abstract methods declared in the {@link java.util.Map} interface.
 */
public interface AbstractTypeConverters {

    <T> void put(final Class<T> cls, final TypeConverter<T> typeConverter);

    <T> boolean putIfAbsent(final Class<T> cls, final TypeConverter<T> typeConverter);

    <T> TypeConverter<T> get(final Class<T> cls);

    <T> boolean exists(final Class<T> cls);
}