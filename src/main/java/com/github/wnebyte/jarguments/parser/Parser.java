package com.github.wnebyte.jarguments.parser;

import java.util.*;
import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.exception.*;
import com.github.wnebyte.jarguments.util.Pair;
import com.github.wnebyte.jarguments.util.Sets;
import com.github.wnebyte.jarguments.util.Strings;
import static com.github.wnebyte.jarguments.ArgumentSupport.matches;
import static com.github.wnebyte.jarguments.ArgumentSupport.getByName;
import static com.github.wnebyte.jarguments.ArgumentSupport.getByPosition;

/**
 * This class is the default implementation of the <code>AbstractParser</code> interface.
 * It can be used to parse and initialize the sub-types of {@link Argument} found
 * in the {@link com.github.wnebyte.jarguments} package.
 */
public class Parser implements AbstractParser {

    /*
    ###########################
    #      UTILITY METHODS    #
    ###########################
    */

    /**
     * Attempts to initialize the specified <code>Argument</code> using the specified <code>token</code>.
     * @param argument an Argument to be initialized.
     * @param token a value to initialize the Argument with.
     * @param input the initial input string, is used to format any exceptions.
     * @return the initialized Argument value.
     * @throws ParseException if initialization fails.
     */
    protected static Object initialize(Argument argument, String token, String input) throws ParseException {
        try {
            return ArgumentSupport.initialize(argument, token);
        } catch (ParseException e) {
            e.setArgument(argument);
            e.setToken(token);
            e.setInput(input);
            throw e;
        }
    }

    /**
     * Returns whether the specified <code>array</code> does not contains any
     * <code>null</code> elements.
     * @param array an array.
     * @return <code>true</code> if the array does not contain any null elements,
     * otherwise <code>false</code>.
     */
    private static boolean elementsNonNull(Object[] array) {
        for (Object o : array) {
            if (o == null)
                return false;
        }
        return true;
    }

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    /**
     * Constructs a new instance.
     */
    public Parser() { }

    /*
    ###########################
    #          METHODS        #
    ###########################
    */

    /**
     * Attempts to initialize the specified <code>batch</code>.
     * @return an array of initialized Argument values.
     * @throws ParseException if initialization fails.
     */
    private Object[] initialize(Map<Argument, Pair<String, String>> batch) throws ParseException {
        if (batch == null || batch.size() == 0) {
            return new Object[0];
        }
        Object[] values = new Object[batch.size()];
        for (Map.Entry<Argument, Pair<String, String>> entry : batch.entrySet()) {
            values[entry.getKey().getIndex()]
                    = initialize(entry.getKey(), entry.getValue().getFirst(), entry.getValue().getSecond());
        }
        assert elementsNonNull(values);
        return values;
    }

    /**
     * Attempts to parse and initialize the specified <code>tokens</code>, <code>arguments</code>.
     * @param input initial src of the tokens, is used to format any exceptions.
     * @param tokens to be parsed.
     * @param arguments to be parsed.
     * @return an array of initialized arguments.
     * @throws ParseException if parsing fails.
     */
    @Override
    public Object[] parse(
            String input,
            Iterable<String> tokens,
            Iterable<Argument> arguments) throws ParseException
    {
        if (tokens == null || arguments == null)
            throw new NullPointerException(
                    "TokenSequence and/or Arguments must not be null."
            );
        Iterator<String> it = tokens.iterator();
        Set<Argument> src = Sets.toSet(arguments);
        Map<Argument, Pair<String, String>> batch = new HashMap<>(src.size());
        int pos = 0;

        // iterate each token
        while (it.hasNext()) {
            final String token = it.next();
            String value, keyValue;
            Argument arg = getByName(src, token);
            arg = (arg != null) ? arg : getByPosition(src, pos++);

            if (arg == null) {
                throw new NoSuchArgumentException(
                        String.format(
                                "Argument with name: '%s' does not exists.", token
                        ), arg, token, input
                );
            }
            else if (arg instanceof Flag || arg instanceof Positional) {
                value = token;
                keyValue = token;
            }
            else {
                if (!it.hasNext()) {
                    throw new MissingArgumentException(
                            String.format(
                                    "Argument: '%s' requires a value.", token
                            ), arg, token, input
                    );
                }
                value = it.next();
                keyValue = token.concat(Strings.WHITESPACE).concat(value);
            }
            if (!matches(arg, keyValue)) {
                throw new MalformedArgumentException(
                        String.format(
                                "Argument: '%s' is malformed.", token
                        ), arg, token, input
                );
            }
            src.remove(arg);
            batch.put(arg, new Pair<>(value, input));
        }

        // iterate each remaining argument
        for (Argument arg : src) {
            if (!(arg instanceof Optional)) {
                throw new MissingArgumentException(
                        "Arguments that are required have to be specified.",
                        arg, null, input
                );
            }
            batch.put(arg, new Pair<>(Strings.EMPTY, input));
        }

        return initialize(batch);
    }
}
