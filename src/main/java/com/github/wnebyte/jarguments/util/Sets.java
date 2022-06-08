package com.github.wnebyte.jarguments.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Sets {

    public static <T> Set<T> newSet() {
        return new HashSet<T>();
    }

    public static <T> LinkedHashSet<T> newLinkedHashSet() {
        return new LinkedHashSet<T>();
    }

    public static <T extends Comparable<? super T>> TreeSet<T> newTreeSet() {
        return new TreeSet<T>();
    }

    public static <T extends Comparable<? super T>> TreeSet<T> newTreeSet(Comparator<? super T> comparator) {
        return new TreeSet<T>(comparator);
    }

    @SafeVarargs
    public static <T> Set<T> of(T... e) {
        return (e == null) ? null : new HashSet<T>(Arrays.asList(e));
    }

    public static <T> Set<T> toSet(Iterable<T> iterable) {
        return (iterable == null) ? newSet() :
                StreamSupport.stream(iterable.spliterator(), false)
                        .collect(Collectors.toSet());
    }

    @SafeVarargs
    public static <T> LinkedHashSet<T> ofLinkedHashSet(T... e) {
        return (e == null) ? null : new LinkedHashSet<T>(Arrays.asList(e));
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> TreeSet<T> ofTreeSet(T... e) {
        return (e == null) ? null : new TreeSet<T>(Arrays.asList(e));
    }

    public static String toString(Set<?> set) {
        return (set == null) ? null : Arrays.toString(set.toArray());
    }
}
