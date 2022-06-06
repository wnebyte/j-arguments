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
import static com.github.wnebyte.jarguments.util.Collections.toLinkedList;

/**
 * This class is the default implementation of the <code>AbstractParser</code> class.
 */
public class Parser implements AbstractParser {

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
                    e,
                    argument,
                    token,
                    input
            );
        }
    }

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
        Object[] values = new Object[map.size()];
        for (Map.Entry<Argument, Pair<String, String>> entry : map.entrySet()) {
            values[entry.getKey().getIndex()]
                    = initialize(entry.getKey(), entry.getValue().getFirst(), entry.getValue().getSecond());
        }
        assert elementsNonNull(values);
        return values;
    }

    @Override
    public void parse(
            String input,
            Iterable<String> tokens,
            Iterable<Argument> arguments) throws ParseException
    {
        if (tokens == null || arguments == null)
            throw new NullPointerException(
                    "TokenSequence & Arguments must not be null."
            );
        Iterator<String> it = tokens.iterator();
        Collection<Argument> source = toLinkedList(arguments);
        map = new HashMap<>(source.size());
        int pos = 0;

        while (it.hasNext()) {
            final String token = it.next();
            String value;
            String keyValue;
            Argument arg = getByName(source, token);
            arg = (arg != null) ? arg : getByPosition(source, pos++);

            if (arg == null) {
                throw new NoSuchArgumentException(
                        String.format(
                                "No such Argument with name: '%s'.", token
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
                                    "Value is required for %s.", token
                            ), input, arg
                    );
                }
                value = it.next();
                keyValue = token.concat(Strings.WHITESPACE).concat(value);
            }
            if (!matches(arg, keyValue)) {
                throw new MalformedArgumentException(
                        String.format(
                                "%s is malformed.", token
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
                                "%s is required.", arg.getCanonicalName()
                        ), input, arg
                );
            }
            map.put(arg, new Pair<>(Strings.EMPTY, input));
        }
    }

    @Override
    public void reset() {
        if (map != null) {
            map.clear();
        }
    }
}
