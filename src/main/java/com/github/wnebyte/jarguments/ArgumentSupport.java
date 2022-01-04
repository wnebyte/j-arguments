package com.github.wnebyte.jarguments;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.github.wnebyte.jarguments.exception.ParseException;
import static com.github.wnebyte.jarguments.util.Collections.isNullOrEmpty;

/**
 * Support class for working with instances of {@link Argument}.
 */
public class ArgumentSupport {

    /**
     * Maps each <code>Argument</code> to its regex.
     * @param c the arguments.
     * @return the result.
     */
    public static LinkedList<String> mapToRegex(Collection<? extends Argument> c) {
        if (isNullOrEmpty(c)) {
            return new LinkedList<>();
        }
        return c.stream().map(Argument::getRegex)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Maps each <code>Argument</code> that is of one of the specified subclasses to its regex.
     * @param c arguments.
     * @param sClasses subclasses of which are to be included in the mapping.
     * @return the result.
     */
    @SafeVarargs
    public static LinkedList<String> mapToRegex(
            Collection<? extends Argument> c, Class<? extends Argument>... sClasses
    ) {
        if ((isNullOrEmpty(c)) || (isNullOrEmpty(sClasses))) {
            return new LinkedList<>();
        }
        List<Class<? extends Argument>> list = Arrays.asList(sClasses);
        return c.stream().filter(arg -> list.contains(arg.getClass()))
                .map(Argument::getRegex)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Maps each <code>Argument</code> that 'passes' the specified predicate to its regex.
     * @param c the arguments.
     * @param p the predicate.
     * @return the result.
     */
    public static LinkedList<String> mapToRegex(Collection<Argument> c, Predicate<Argument> p) {
        if (isNullOrEmpty(c)) {
            return new LinkedList<>();
        }
        return c.stream().filter(p).map(Argument::getRegex)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Casts and returns the Arguments from the specified <code>Collection</code> whose <code>class</code>
     * <code>equals</code> the specified <code>class</code>.
     * @param c the arguments.
     * @param sClass the class.
     * @param <T> the type of the class.
     * @return the arguments from the specified <code>Collection</code> whose <code>class</code>
     * <code>equals</code> the specified <code>class</code>.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Argument> List<T> getInstancesOfSubClass(Collection<Argument> c, Class<T> sClass) {
        if ((isNullOrEmpty(c)) || (sClass == null)) {
            return new ArrayList<>(0);
        }
        return c.stream().filter(arg -> arg.getClass().equals(sClass))
                .map(arg -> (T)arg)
                .collect(Collectors.toList());
    }

    /**
     * Returns whether the specified <code>Collection</code> <code>contains</code> any elements that are of a class
     * contained within the specified <code>sClasses</code>.
     * @param c the elements.
     * @param sClasses the classes.
     * @return <code>true</code> if the specified <code>Collection</code> <code>contains</code> one or more elements
     * that are of a class contained within the specified <code>sClasses</code>, otherwise <code>false</code>.
     */
    public static boolean containsInstancesOfSubClasses(
            Collection<Argument> c, Collection<Class<? extends Argument>> sClasses
    ) {
        if ((isNullOrEmpty(c)) || (isNullOrEmpty(sClasses))) {
            return false;
        }
        return c.stream().anyMatch(arg -> sClasses.contains(arg.getClass()));
    }

    /**
     * Returns the first <code>Argument</code> from the specified <code>Collection</code> whose index
     * <code>equals</code> the specified <code>index</code>.
     * @param c the elements.
     * @param index the index.
     * @return the first <code>Argument</code> from the specified <code>Collection</code> whose index
     * <code>equals</code> the specified <code>index</code> if one exists, otherwise <code>null</code>.
     */
    public static Argument getByIndex(Collection<Argument> c, int index) {
        if ((isNullOrEmpty(c)) || (index < 0)) {
            return null;
        }
        return c.stream().filter(arg -> arg.getIndex() == index)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the first <code>Argument</code> from the specified <code>Collection</code> whose set of names
     * <code>contains</code> the specified <code>name</code>.
     * @param c the elements.
     * @param name the name.
     * @return the first <code>Argument</code> from the specified <code>Collection</code> whose set of names
     * <code>contains</code> the specified <code>name</code> if one exists, otherwise <code>null</code>.
     */
    public static Argument getByName(Collection<Argument> c, String name) {
        if ((isNullOrEmpty(c)) || (name == null)) {
            return null;
        }
        return c.stream().filter(arg -> arg.getNames().contains(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the first <code>Positional</code> from the specified <code>Collection</code> whose position
     * <code>equals</code> the specified <code>position</code>.
     * @param c the elements.
     * @param position the position.
     * @return the first <code>Positional</code> whose position <code>equals</code> the specified
     * <code>position</code> if one exists, otherwise <code>null</code>.
     */
    public static Argument getByPosition(Collection<Argument> c, int position) {
        if ((isNullOrEmpty(c)) || (position < 0)) {
            return null;
        }
        return c.stream().filter(arg -> arg instanceof Positional)
                .map(arg -> (Positional) arg)
                .filter(arg -> arg.getPosition() == position)
                .findFirst()
                .orElse(null);

    }

    /**
     * Initializes the specified <code>Argument</code> using the specified <code>String</code>.
     * @param argument to be initialized.
     * @param value to initialize with.
     * @return the result from the initialization.
     * @throws ParseException if the initialization fails.
     */
    public static Object initialize(Argument argument, String value) throws ParseException {
        return argument.initialize(value);
    }

    public static boolean matches(Argument argument, String value) {
        return argument.matches(value);
    }

    public static String getRegex(Argument argument) {
        return argument.getRegex();
    }
}