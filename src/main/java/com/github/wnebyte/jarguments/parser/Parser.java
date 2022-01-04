package com.github.wnebyte.jarguments.parser;

import java.util.*;
import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.exception.*;
import com.github.wnebyte.jarguments.util.Pair;
import com.github.wnebyte.jarguments.util.Strings;
import static com.github.wnebyte.jarguments.ArgumentSupport.matches;
import static com.github.wnebyte.jarguments.ArgumentSupport.getByName;
import static com.github.wnebyte.jarguments.ArgumentSupport.getByPosition;

/**
 * This class is the default implementation of the <code>AbstractParser</code> class.
 */
public class Parser extends AbstractParser<Tokens, Collection<Argument>> {

    /*
    ###########################
    #      UTILITY METHODS    #
    ###########################
    */

    protected static Object initialize(Argument argument, String token, String input) throws ParseException {
        try {
            return ArgumentSupport.initialize(argument, token);
        } catch (ParseException e) {
            throw new TypeConversionException(
                    e.getMessage(),
                    argument,
                    token,
                    input
            );
        }
    }

    private static boolean elementsNonNull(Object[] arr) {
        for (Object o : arr) {
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

    private Map<Argument, Pair<String, String>> map;

    /*
    ###########################
    #          METHODS        #
    ###########################
    */

    @Override
    public Object[] initialize() throws ParseException {
        if (map == null) {
            return new Object[0];
        }
        Object[] arr = new Object[map.size()];
        for (Map.Entry<Argument, Pair<String, String>> entry : map.entrySet()) {
            arr[entry.getKey().getIndex()]
                    = initialize(entry.getKey(), entry.getValue().getFirst(), entry.getValue().getSecond());
        }
        assert elementsNonNull(arr);
        return arr;
    }

    @Override
    public void parse(Tokens tokens, Collection<Argument> arguments) throws ParseException {
        if ((tokens == null) || (arguments == null))
            throw new NullPointerException(
                    "Tokens & Arguments must not be null."
            );
        Iterator<String> it = tokens.iterator();
        Collection<Argument> source = new LinkedList<>(arguments);
        String input = tokens.join();
        map = new HashMap<>(source.size());
        int pos = 0;

        while (it.hasNext()) {
            final String token = it.next();
            String value;
            Argument arg = getByName(source, token);
            arg = (arg != null) ? arg : getByPosition(source, pos++);

            if (arg == null) {
                throw new NoSuchArgumentException(
                        String.format(
                                "No Argument with name/value: '%s' remain to be parsed.", token
                        ), input, token
                );
            }
            else if ((arg instanceof Flag) || (arg instanceof Positional)) {
                value = token;
            }
            else {
                if (!it.hasNext()) {
                    throw new MissingArgumentValueException(
                            String.format(
                                    "Argument with name: '%s' is missing a value.", token
                            ), input, arg
                    );
                }
                value = token.concat(Strings.WHITESPACE).concat(it.next());
            }
            if (!matches(arg, Strings.WHITESPACE.concat(value))) {
                throw new MalformedArgumentException(
                        String.format(
                                "Argument with name: '%s' is malformed.", token
                        ), input, arg
                );
            }
            source.remove(arg);
            map.put(arg, new Pair<>(value, input));
        }

        for (Argument arg : source) {
            if (!(arg instanceof Optional)) {
                throw new MissingArgumentException(
                        String.format(
                                "Argument: '%s' must be specified.", arg
                        ), input, arg
                );
            }
            map.put(arg, new Pair<>("", input));
        }
    }

    @Override
    public void reset() {
        if (map != null) {
            map.clear();
        }
    }
}
