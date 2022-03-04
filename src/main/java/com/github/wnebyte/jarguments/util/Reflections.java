package com.github.wnebyte.jarguments.util;

import java.util.Arrays;
import java.util.List;

/**
 * This class is a utility class for working with reflection.
 */
public class Reflections {

    private static final List<Class<?>> WRAPPER_CLASSES = Arrays.asList(
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

    /**
     * Determines whether the specified <code>Class</code> represents one of
     * <code>boolean.class</code> or <code>Boolean.class</code>.
     * @param cls the class.
     * @return <code>true</code> if the specified class represents one of
     * boolean.class or Boolean.class,
     * otherwise <code>false</code>.
     */
    public static boolean isBoolean(Class<?> cls) {
        return (cls == boolean.class || cls == Boolean.class);
    }

    /**
     * Determines whether the specified <code>Class</code> represents a primitive class or a
     * wrapper class (string included).
     * @param cls the class.
     * @return <code>true</code> if the specified class represents a primitive class or a
     * wrapper class,
     * otherwise <code>false</code>.
     */
    public static boolean isPrimitive(Class<?> cls) {
        return (cls != null) && (cls.isPrimitive() || WRAPPER_CLASSES.contains(cls));
    }

    /**
     * Determines whether the specified <code>Class</code> represents an array class.
     * @param cls the class.
     * @return <code>true</code> if the specified class represents an array class,
     * otherwise <code>false</code>.
     */
    public static boolean isArray(Class<?> cls) {
        return (cls != null) && (cls.isArray());
    }

    /**
     * Determines whether <code>Iterable.class</code> is assignable from the specified <code>Class</code>.
     * @param cls the class.
     * @return <code>true</code> if Iterable.class is assignable from the specified Class,
     * otherwise <code>false</code>.
     */
    public static boolean isIterable(Class<?> cls) {
        return (cls != null) && (Iterable.class.isAssignableFrom(cls));
    }

    /**
     * Determines whether the specified <code>Class</code> represents an array class,
     * or <code>Iterable.class</code> is assignable from the specified <code>Class</code>.
     * @param cls the class.
     * @return <code>true</code> if the specified Class represents an array class
     * or if Iterable.class is assignable from the specified Class,
     * otherwise <code>false</code>.
     */
    public static boolean isArrayOrIterable(Class<?> cls) {
        return (isArray(cls) || isIterable(cls));
    }
}