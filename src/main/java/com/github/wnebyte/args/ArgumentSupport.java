package com.github.wnebyte.args;

import java.util.*;
import java.util.stream.Collectors;
import com.github.wnebyte.args.util.Collections;

public class ArgumentSupport {

    /**
     * Returns a <code>LinkedList</code> of regular expressions mapped from the elements of the
     * specified <code>arguments</code> collection, that are of the same class as one of the specified <code>cls</code>.
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

    public static LinkedList<String> mapToRegexList(
            final Collection<Argument> args
    ) {
        if (Collections.isNullOrEmpty(args)) {
            return new LinkedList<>();
        }
        return args.stream().map(Argument::getRegex).collect(Collectors.toCollection(LinkedList::new));
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
}