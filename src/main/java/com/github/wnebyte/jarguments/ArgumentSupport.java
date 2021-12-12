package com.github.wnebyte.jarguments;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import com.github.wnebyte.jarguments.util.Collections;

/**
 * Support class for working with instances of {@link Argument}.
 */
public class ArgumentSupport {

    /**
     * Maps each of the specified <code>args</code> to their regex.
     * @param args to be mapped.
     * @param sClasses sub classes to be mapped.
     * @return a <code>LinkedList</code> of regex's.
     */
    @SafeVarargs
    public static LinkedList<String> mapToRegexList(
            final Collection<Argument> args,
            final Class<? extends Argument>... sClasses
    ) {
        if ((Collections.isNullOrEmpty(args)) || (Collections.isNullOrEmpty(sClasses))) {
            return new LinkedList<>();
        }
        List<Class<? extends Argument>> sClassesList = Arrays.asList(sClasses);
        return args.stream()
                .filter(arg -> sClassesList.contains(arg.getClass()))
                .map(Argument::getRegex)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Maps each of the specified <code>args</code> to their regex.
     * @param args to be mapped.
     * @return a <code>LinkedList</code> of regex's.
     */
    public static LinkedList<String> mapToRegexList(
            final Collection<Argument> args
    ) {
        if (Collections.isNullOrEmpty(args)) {
            return new LinkedList<>();
        }
        return args.stream().map(Argument::getRegex).collect(Collectors.toCollection(LinkedList::new));
    }

    public static LinkedList<String> mapToRegexList(
            final Collection<Argument> args,
            final Predicate<Argument> predicate
            ) {
        if (Collections.isNullOrEmpty(args)) {
            return new LinkedList<>();
        }
        return args.stream().filter(predicate).map(Argument::getRegex)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Argument> List<T> getInstancesOfSubClass(
            final Collection<Argument> args,
            final Class<T> sClass
    ) {
        if ((Collections.isNullOrEmpty(args)) || (sClass == null)) {
            return new ArrayList<>(0);
        }
        return args.stream()
                .filter(arg -> arg.getClass().equals(sClass))
                .map(arg -> (T)arg)
                .collect(Collectors.toList());
    }

    public static boolean containsInstancesOfSubClasses(
            final Collection<Argument> args,
            final Collection<Class<? extends Argument>> sClasses
    ) {
        if ((Collections.isNullOrEmpty(args)) || (Collections.isNullOrEmpty(sClasses))) {
            return false;
        }
        return args.stream().anyMatch(arg -> sClasses.contains(arg.getClass()));
    }

    public static Argument getByIndex(final Collection<Argument> args, final int index) {
        if ((Collections.isNullOrEmpty(args)) || (index < 0)) {
            return null;
        }
        return args.stream()
                .filter(arg -> arg.getIndex() == index)
                .findFirst()
                .orElse(null);
    }

    public static Argument getByName(final Collection<Argument> args, final String name) {
        if ((Collections.isNullOrEmpty(args)) || (name == null)) {
            return null;
        }
        return args.stream()
                .filter(arg -> arg.getNames().contains(name))
                .findFirst()
                .orElse(null);
    }

    public static Argument getByPosition(final Collection<Argument> args, final int position) {
        if ((Collections.isNullOrEmpty(args)) || (position < 0)) {
            return null;
        }
        return args.stream()
                .filter(arg -> arg instanceof Positional)
                .map(arg -> (Positional) args)
                .filter(arg -> arg.getPosition() == position)
                .findFirst()
                .orElse(null);

    }
}