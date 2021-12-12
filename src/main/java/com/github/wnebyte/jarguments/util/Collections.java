package com.github.wnebyte.jarguments.util;

import java.util.Collection;
import java.util.List;

public class Collections {

    public static <T> boolean intersects(final Collection<T> c1, final Collection<T> c2) {
        if ((isNullOrEmpty(c1)) || (isNullOrEmpty(c2))) {
            return false;
        }
        for (T t : c1) {
            if (c2.contains(t)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNullOrEmpty(final Collection<?> c) {
        return ((c == null) || (c.isEmpty()));
    }

    public static boolean isNullOrEmpty(final Object[] c) {
        return ((c == null) || (c.length == 0));
    }

    public static <T> T getElementOrDefaultValue(final List<T> list, final int index, final T defaultValue) {
        if ((list == null) || (list.isEmpty()) || (index < 0)) {
            return defaultValue;
        }
        return (index <= list.size() - 1) ? list.get(index) : defaultValue;
    }
}
