package com.github.wnebyte.jarguments.util;

import java.util.Arrays;
import java.util.List;

public class Reflections {

    public static boolean isEnumerable(Class<?> cls) {
        return isArray(cls) || isIterable(cls);
    }

    public static boolean isArray(Class<?> cls) {
        return (cls != null) && (cls.isArray());
    }

    public static boolean isIterable(Class<?> cls) {
        return (cls != null) && (Iterable.class.isAssignableFrom(cls));
    }

    public static boolean isBoolean(Class<?> cls) {
        return (cls == boolean.class || cls == Boolean.class);
    }

    public static boolean isPrimitive(Class<?> cls) {
        return (cls != null) && (cls.isPrimitive() || isWrapperClass(cls));
    }

    public static boolean isWrapperClass(Class<?> cls) {
        List<Class<?>> classes = Arrays.asList(
                Boolean.class,
                Byte.class,
                Character.class,
                Double.class,
                Float.class,
                Integer.class,
                Long.class,
                Short.class,
                String.class
        );
        return classes.contains(cls);
    }
}