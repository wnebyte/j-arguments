package com.github.wnebyte.args;

import com.github.wnebyte.args.exception.ConstraintException;
import com.github.wnebyte.args.exception.ParseException;
import com.github.wnebyte.args.util.Collections;
import com.github.wnebyte.args.util.Strings;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a parser impl whose purpose it is to initialize subclasses of {@link Argument}.
 */
public class Parser implements IParser {

    private final List<Argument> mutableArgs;

    /**
     * Constructs a new instance using the specified <code>args</code>.
     */
    public Parser(final List<Argument> args) {
        this.mutableArgs = new LinkedList<>(args);
    }

    /**
     * Initializes the underlying arguments by parsing the specified <code>input</code>.
     * @param input to be parsed.
     * @return the initialized arguments.
     * @throws ParseException if any of the underlying arguments fail to convert.
     * @throws ConstraintException if any of the underlying arguments have constraints that fail to hold.
     */
    public Object[] parse(final String input) throws ParseException, ConstraintException {
        Object[] args = new Object[mutableArgs.size()];
        List<Positional> positionalArgs =
                ArgumentSupport.getInstancesOfSubClass(mutableArgs, Positional.class);
        LinkedList<String> values = split(input);

         // initialize any positional arguments first.
        for (Positional arg : positionalArgs) {
            String value = values.pop();
            mutableArgs.remove(arg);
            init(args, input, arg, value);
        }
        // grep and initialize the argument whose collection of names contain the iterated value.
        for (int i = 0; i < values.size(); i++) {
            final String value = values.get(i);
            String val;
            Argument arg = ArgumentSupport.getByName(mutableArgs, value);
            if (arg == null) {
                throw new ParseException(
                        "There is no argument with name '" + value + "'."
                );
            }
            // get the next element if one exists, else default value.
            String nextVal = Collections.getNextElement(values, i++, null);
            if (nextVal != null) {
                val = value.concat(" ").concat(nextVal);
            } else {
                val = value;
            }
            mutableArgs.remove(arg);
            init(args, input, arg, val);
        }
        // initialize any remaining optional arguments.
        for (Argument arg : mutableArgs) {
            if (arg instanceof Optional) {
                String val = "";
                init(args, input, arg, val);
            }
        }
        return args;
    }

    private void init(Object[] args, String input, Argument arg, String value) throws ParseException {
        try {
            args[arg.getIndex()] = arg.initialize(value);
        } catch (ParseException e) {
            e.setArgument(arg);
            e.setInput(input);
            e.setValue(value);
            throw e;
        }
    }

    /**
     * Splits the specified <code>input</code> into a collection of Strings.
     * @param input to be split.
     * @return the result.
     */
    @Override
    public LinkedList<String> split(final String input) {
        return new LinkedList<>(Strings.split(input, ' ', Arrays.asList('"', '\'')));
    }
}