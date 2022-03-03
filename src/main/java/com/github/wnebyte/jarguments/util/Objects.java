package com.github.wnebyte.jarguments.util;

import java.util.function.Supplier;

/**
 * This class is a utility class for working with instances of {@link Object}.
 */
public class Objects {

    /**
     * Returns the specified <code>value</code> if it is non <code>null</code>,
     * otherwise returns the result of calling the specified <code>supplier</code>.
     * @param value a value.
     * @param supplier a supplier.
     * @param <T> the type of the value.
     * @return the specified value if it is non <code>null</code>,
     * otherwise the result of calling the specified supplier.
     */
    public static <T> T requireNonNullElseGet(T value, Supplier<T> supplier) {
        return (value != null) ? value : supplier.get();
    }

    /**
     * Returns <code>true</code> if the arguments are equal to each other and <code>false</code>
     * otherwise. Consequently, if both arguments are <code>null</code>, <code>true</code> is returned
     * and if exactly one argument is <code>null</code>, <code>false</code> is returned. Otherwise,
     * equality is determined by using the {@linkplain Object#equals(Object)} method on the first argument.
     * @param a an object.
     * @param b an object to be compared with <code>a</code> for equality.
     * @param <T> the type of the two objects.
     * @return <code>true</code> if the arguments are equal to each other,
     * otherwise <code>false</code>.
     */
    public static <T> boolean equals(T a, T b) {
        if ((a != null) && (b != null)) {
            return a.equals(b);
        }
        return (a == null) && (b == null);
    }

    /**
     * Returns the hash code of the specified <code>Object</code> if it is non <code>null</code>,
     * otherwise <code>0</code>.
     * @param o an object.
     * @param <T> the type of the object.
     * @return the hash code of the specified object if it is non <code>null</code>,
     * otherwise <code>0</code>.
     */
    public static <T> int hashCode(T o) {
        return (o != null) ? o.hashCode() : 0;
    }
}