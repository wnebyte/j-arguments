package com.github.wnebyte.jarguments.util;

import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Sets {

    public static <T> Set<T> of(T[] array) {
        return (array == null) ? null : new HashSet<T>(Arrays.asList(array));
    }

    public static <T> Set<T> ofLinkedHashSet(T[] array) {
        return (array == null) ? null : new LinkedHashSet<>(Arrays.asList(array));
    }

    public static String toString(Set<?> set) {
        return (set == null) ? null : Arrays.toString(set.toArray());
    }
}
