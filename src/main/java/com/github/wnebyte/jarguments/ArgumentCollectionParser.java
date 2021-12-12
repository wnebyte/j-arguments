package com.github.wnebyte.jarguments;

import java.util.*;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.exception.TypeConversionException;
import com.github.wnebyte.jarguments.util.Collections;
import com.github.wnebyte.jarguments.util.Strings;
import jdk.nashorn.internal.runtime.ParserException;

import static com.github.wnebyte.jarguments.ArgumentSupport.*;

public class ArgumentCollectionParser extends AbstractParser<Collection<Argument>> {

    public ArgumentCollectionParser(Collection<Argument> source) {
        super(new ArrayList<>(source));
    }

    public Object[] parse(String input) throws ParseException {
        Object[] args = new Object[getSource().size()];
        List<Positional> positionalArgs = getInstancesOfSubClass(getSource(), Positional.class);
        LinkedList<String> values = split(input);

        for (Positional argument : positionalArgs) {
            String value = values.pop();
            getSource().remove(argument);
            args[argument.getIndex()] = initialize(argument, input, value);
        }

        for (int i = 0; i < values.size(); i++) {
            final String value = values.get(i);
            String val;
            Argument argument = getSource().stream().filter(arg -> arg.getNames().contains(value)).findFirst()
                    .orElseThrow(() -> new ParseException(
                            "Unable to parse Argument with name: " + value + "."
                    ));
            if (argument instanceof Flag) {
                val = value;
            }
            else {
                val = value.concat((i + 1) < values.size() ?
                        " ".concat(values.get(i + 1)) : "");
                i++;
            }
            getSource().remove(argument);
            args[argument.getIndex()] = initialize(argument, input, val);
        }

        for (Argument argument : getSource()) {
            if (argument instanceof Optional) {
                args[argument.getIndex()] = initialize(argument, input, "");
            }
        }

        return args;
    }

    protected final Object initialize(Argument argument, String input, String value) throws ParseException {
        try {
            return argument.initialize(value);
        } catch (ParseException e) {
            throw new TypeConversionException(
                    e.getMessage(),
                    argument,
                    input,
                    value
            );
        }
    }

    @Override
    protected LinkedList<String> split(String input) {
        List<String> c = Strings.split(input, ' ', Arrays.asList('"', '\''));
        return new LinkedList<>(c);
    }
}