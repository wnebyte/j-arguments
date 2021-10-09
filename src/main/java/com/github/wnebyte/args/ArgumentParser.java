package com.github.wnebyte.args;

import com.github.wnebyte.args.exception.ConstraintException;
import com.github.wnebyte.args.exception.ParseException;
import com.github.wnebyte.args.util.Collections;
import com.github.wnebyte.args.util.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a parser whose purpose it is to parse instances of {@link Argument} class.
 */
public class ArgumentParser implements Parser {

    private final List<Argument> arguments;

    /**
     * Constructs a new instance using the specified <code>arguments</code>.
     */
    public ArgumentParser(final List<Argument> arguments) {
        this.arguments = new ArrayList<>(arguments);
    }

    /**
     * Parses the specified <code>input</code> and initializes any underlying arguments.
     * @param input to be parsed.
     * @return the initialized arguments.
     * @throws ParseException if any of the underlying arguments fail to convert.
     * @throws ConstraintException if any of the underlying arguments have constraints that fail to hold.
     */
    public Object[] parse(final String input) throws ParseException, ConstraintException {
        Object[] args = new Object[arguments.size()];
        List<Positional> positionalArguments = ArgumentUtil.getSubclasses(arguments, Positional.class);
        LinkedList<String> values = split(input);

         // initialize any positional arguments first.
        for (Positional argument : positionalArguments) {
            String value = values.pop();
            arguments.remove(argument);
            try {
                args[argument.getIndex()] = argument.initialize(value);
            } catch (ParseException e) {
                e.setArgument(argument);
                e.setInput(input);
                e.setValue(value);
                throw e;
            }
        }
        // grep and initialize the argument whose collection of names contain the iterated value.
        for (int i = 0; i < values.size(); i++) {
            final String value = values.get(i);
            String val;
            Argument argument = ArgumentUtil.getByName(arguments, value);
            if (argument == null) {
                throw new ParseException(
                        "There is no argument with name '" + value + "'"
                );
            }
            // get the next value and increment the counter.
            String next = Collections.getNextElement(values, i++, null);
            if (next != null) {
                val = value + " " + next;
            } else {
                val = value;
            }
            arguments.remove(argument);
            try {
                args[argument.getIndex()] = argument.initialize(val);
            } catch (ParseException e) {
                e.setArgument(argument);
                e.setInput(input);
                e.setValue(next);
                throw e;
            }
        }
        // initialize any remaining optional arguments.
        for (Argument argument : arguments) {
            if (argument instanceof Optional) {
                String val = "";
                try {
                    args[argument.getIndex()] = argument.initialize(val);
                } catch (ParseException e) {
                    e.setArgument(argument);
                    e.setInput(input);
                    e.setValue(val);
                    throw e;
                }
            }
        }
        return args;
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