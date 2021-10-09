package com.github.wnebyte.args.util;

import java.util.Collection;
import java.util.List;

public class Collections {

    public static <T> boolean intersects(final Collection<T> c1, final Collection<T> c2) {
        if ((nullOrEmpty(c1)) || (nullOrEmpty(c2))) {
            return false;
        }
        for (T t : c1) {
            if (c2.contains(t)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean nullOrEmpty(final Collection<T> c) {
        return ((c == null) || (c.isEmpty()));
    }

    public static <T> T getNextElement(final List<T> list, final int index, final T defaultValue) {
        if ((list == null) || (list.isEmpty()) || (index < 0)) {
            return defaultValue;
        }
        return (index < list.size() - 1) ? list.get(index + 1) : defaultValue;
    }
}
