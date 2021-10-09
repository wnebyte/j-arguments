package com.github.wnebyte.args.util;

import java.util.Arrays;
import java.util.Collection;

public class Reflections {

    public static boolean isArray(final Class<?> cls) {
        return (cls != null) && (cls.isArray());
    }

    public static boolean isCollection(final Class<?> cls) {
        return (cls != null) && (Collection.class.isAssignableFrom(cls));
    }

    public static boolean isIterable(final Class<?> cls) {
        return (cls != null) && (Iterable.class.isAssignableFrom(cls));
    }

    public static boolean isBoolean(final Class<?> cls) {
        return (cls == boolean.class || cls == Boolean.class);
    }

    public static boolean isArrayOrCollection(final Class<?> cls) {
        return (isArray(cls)) || (isCollection(cls));
    }

    public static boolean isPrimitive(final Class<?> cls) {
        return (cls != null) && (cls.isPrimitive() || isWrapperClass(cls));
    }

    public static boolean isWrapperClass(final Class<?> cls) {
        return Arrays.asList(
                Boolean.class,
                Byte.class,
                Character.class,
                Double.class,
                Float.class,
                Integer.class,
                Long.class,
                Short.class,
                String.class
        ).contains(cls);
    }

    public static boolean isObject(final Class<?> cls) {
        return !(isArray(cls)) && !(isPrimitive(cls));
    }
}