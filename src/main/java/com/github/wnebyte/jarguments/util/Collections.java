package com.github.wnebyte.jarguments.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This class is a utility class for working with instances of {@link Collection}.
 */
public class Collections {

    /**
     * Determines whether the specified <code>Collection</code> is <code>null</code> or
     * empty.
     * @param c a collection.
     * @return <code>true</code> if the specified Collection is <code>null</code> or
     * empty,
     * otherwise <code>false</code>.
     */
    public static boolean isNullOrEmpty(Collection<?> c) {
        return (c == null || c.isEmpty());
    }

    /**
     * Determines whether the specified Collections intersect.
     * @param c1 a collection.
     * @param c2 a collection to be compared with <code>c1</code> for intersection.
     * @param <T> the element type of the two Collections.
     * @return <code>true</code> if the specified Collections intersect,
     * otherwise <code>false</code>.
     */
    public static <T> boolean intersects(Collection<T> c1, Collection<T> c2) {
        if (isNullOrEmpty(c1) || isNullOrEmpty(c2)) {
            return false;
        }
        for (T t : c1) {
            if (c2.contains(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether any of the elements contained within the specified <code>Collection</code> is <code>null</code>.
     * @param c a Collection.
     * @return <code>true</code> if the specified Collection is <code>null</code> or any of its elements are <code>null</code>,
     * otherwise <code>false</code>.
     */
    public static boolean containsNull(Collection<?> c) {
        if (c == null) {
            return true;
        }
        for (Object o : c) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    public static <T> Collection<T> toCollection(Iterable<T> iterable) {
        return (iterable == null) ? new ArrayList<T>() :
                StreamSupport.stream(iterable.spliterator(), false)
                        .collect(Collectors.toList());
    }

    public static <T> List<T> toList(Iterable<T> iterable) {
        return (List<T>)toCollection(iterable);
    }

    public static <T> LinkedList<T> toLinkedList(Iterable<T> iterable) {
        return (iterable == null) ? new LinkedList<T>() :
                StreamSupport.stream(iterable.spliterator(), false)
                        .collect(Collectors.toCollection(LinkedList::new));
    }
}
