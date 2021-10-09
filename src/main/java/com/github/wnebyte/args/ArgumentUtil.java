package com.github.wnebyte.args;

import java.util.*;
import java.util.stream.Collectors;

public class ArgumentUtil {

    /**
     * Returns a <code>LinkedList</code> of regular expressions mapped from the elements of the
     * specified <code>arguments</code> collection, that are of the same class as one of the specified <code>cls</code>.
     */
    @SafeVarargs
    public static LinkedList<String> getRegularExpressions(
            final Collection<Argument> arguments,
            final Class<? extends Argument>... cls
    ) {
        if ((arguments == null) || (arguments.isEmpty()) || (cls == null) || (cls.length == 0)) {
            return new LinkedList<>();
        }
        List<Class<? extends Argument>> classes = Arrays.asList(cls);
        return arguments
                .stream()
                .filter(arg -> classes.contains(arg.getClass()))
                .map(Argument::getRegex)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Argument> List<T> getSubclasses(
            final Collection<Argument> arguments,
            final Class<T> cls
    ) {
        if ((arguments == null) || (arguments.isEmpty()) || (cls == null)) {
            return new ArrayList<>(0);
        }
        return arguments
                .stream()
                .filter(arg -> arg.getClass().equals(cls))
                .map(arg -> (T)arg)
                .collect(Collectors.toList());
    }

    public static boolean containsSubclasses(
            final Collection<Argument> arguments,
            final Collection<Class<? extends Argument>> classes
    ) {
        if ((arguments == null) || (arguments.isEmpty()) || (classes == null) || (classes.isEmpty())) {
            return false;
        }
        return arguments
                .stream()
                .anyMatch(arg -> classes.contains(arg.getClass()));
    }

    public static Argument getByIndex(final Collection<Argument> arguments, final int index) {
        if ((arguments == null) || (arguments.isEmpty()) || (index < 0)) {
            return null;
        }
        return arguments
                .stream()
                .filter(arg -> arg.getIndex() == index)
                .findFirst()
                .orElse(null);
    }

    public static Positional getByPosition(final Collection<Argument> arguments, final int position) {
        if ((arguments == null) || (arguments.isEmpty()) || (position < 0)) {
            return null;
        }
        return (Positional) arguments
                .stream()
                .filter(arg -> (arg instanceof Positional) && (((Positional) arg).getPosition() == position))
                .findFirst()
                .orElse(null);
    }

    public static Argument getByName(final Collection<Argument> arguments, final String name) {
        if ((arguments == null) || (arguments.isEmpty()) || (name == null)) {
            return null;
        }
        return arguments
                .stream()
                .filter(arg -> arg.getNames().contains(name))
                .findFirst()
                .orElse(null);
    }
}