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
 * Can be used to parse and initialize common subtypes of {@link Argument}.
 */
public class Parser implements AbstractParser {

    /*
    ###########################
    #      UTILITY METHODS    #
    ###########################
    */

    // Todo: fix catch block
    /**
     * Attempts to initialize the specified <code>Argument</code> using the specified <code>token</code>.
     * @param argument an Argument to be initialized.
     * @param token a value to initialize the Argument with.
     * @param input the initial input string, is used to format any exceptions.
     * @return the initialized Argument.
     * @throws ParseException if initialization fails.
     */
    protected static Object initialize(Argument argument, String token, String input) throws ParseException {
        try {
            return ArgumentSupport.initialize(argument, token);
        } catch (ParseException e) {
            throw new TypeConversionException(
                    e,
                    argument,
                    token,
                    input
            );
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
    #          FIELDS         #
    ###########################
    */

    /**
     * Stores the most recently parsed Arguments.
     */
    private final Map<Argument, Pair<String, String>> batch;

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    /**
     * Constructs a new instance.
     */
    public Parser() {
        this.batch = new HashMap<>();
    }

    /*
    ###########################
    #          METHODS        #
    ###########################
    */

    /**
     * Attempts to initialize the batch of Arguments that were most recently parsed.
     * @return an array of initialized Argument values.
     * @throws ParseException if initialization fails.
     */
    @Override
    public Object[] initialize() throws ParseException {
        if (batch.size() == 0) {
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
     * Attempts to parse the specified <code>tokens</code> and <code>arguments</code>.
     * @param input initial src of the tokens, is used to format any exceptions.
     * @param tokens to be parsed.
     * @param arguments to be parsed.
     * @return <code>true</code> if the specified tokens and arguments were successfully parsed,
     * otherwise <code>false</code>.
     * @throws ParseException if parsing fails.
     */
    @Override
    public boolean parse(
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
        batch.clear();
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
                                "Argument: '%s' does not exists.", token
                        ), input, token
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
                            ), input, arg
                    );
                }
                value = it.next();
                keyValue = token.concat(Strings.WHITESPACE).concat(value);
            }
            if (!matches(arg, keyValue)) {
                throw new MalformedArgumentException(
                        String.format(
                                "Argument: '%s' is malformed.", token
                        ), input, arg
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
                        input, arg
                );
            }
            batch.put(arg, new Pair<>(Strings.EMPTY, input));
        }

        return true;
    }

    /**
     * Clears the batch of most recently parsed Arguments.
     */
    @Override
    public void clear() {
        batch.clear();
    }
}
