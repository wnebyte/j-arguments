package com.github.wnebyte.args.util;

import java.util.function.Supplier;

public class Objects {

    public static <T> T requireNonNullElseGet(final T value, final Supplier<T> supplier) {
        return value != null ? value : supplier.get();
    }

    public static <T> boolean equals(final T t1, final T t2) {
        if ((t1 != null) && (t2 != null)) {
            return t1.equals(t2);
        }
        return (t1 == null) && (t2 == null);
    }

    public static <T> int hashCode(final T t) {
        if (t == null) {
            return 0;
        }
        return t.hashCode();
    }

    /**
     * @param objects to be checked for <code>null</code> values.
     * @return <code>true</code> if the specified <code>objects</code> is <code>null</code>, or if it contains
     * at least one <code>null</code> value,
     * else <code>false</code>.
     */
    public static boolean isNull(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (Object o : objects) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }
}