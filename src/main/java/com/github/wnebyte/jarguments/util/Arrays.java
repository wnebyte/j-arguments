package com.github.wnebyte.jarguments.util;

/**
 * This class is a utility class for working with arrays.
 */
public class Arrays {

    /**
     * Determines whether the specified <code>array</code> is <code>null</code> or
     * empty.
     * @param array an array.
     * @return <code>true</code> if the specified array is <code>null</code> or
     * empty,
     * otherwise <code>false</code>.
     */
    public static boolean isNullOrEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * Determines whether the specified <code>array</code> is empty.
     * @param array an array.
     * @return <code>true</code> if the specified array is empty,
     * otherwise <code>false</code>.
     */
    public static boolean isEmpty(Object[] array) {
        return (array.length == 0);
    }

    /**
     * Determines whether any of the elements contained within the specified <code>array</code> is <code>null</code>.
     * @param array an array.
     * @return <code>true</code> if the specified array is <code>null</code> or any of its elements are <code>null</code>,
     * otherwise <code>false</code>.
     */
    public static boolean containsNull(Object... array) {
        if (array == null) {
            return true;
        }
        for (Object o : array) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }
}
