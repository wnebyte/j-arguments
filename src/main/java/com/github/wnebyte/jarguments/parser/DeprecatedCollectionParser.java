package com.github.wnebyte.jarguments.parser;

import java.util.*;

import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.exception.TypeConversionException;
import com.github.wnebyte.jarguments.util.Strings;

import static com.github.wnebyte.jarguments.ArgumentSupport.*;

@Deprecated
public class DeprecatedCollectionParser extends AbstractParser<Collection<Argument>> {

    public DeprecatedCollectionParser(Collection<Argument> source) {
        super(new ArrayList<>(source));
    }

    public DeprecatedCollectionParser() {
        super(new ArrayList<>());
    }

    public Object[] parse(String input) throws ParseException {
        List<Argument> source = new ArrayList<>(super.source);
        Object[] args = new Object[source.size()];
        List<Positional> positionalArgs = getInstancesOfSubClass(source, Positional.class);
        LinkedList<String> values = split(input);

        for (Positional argument : positionalArgs) {
            String value = values.pop();
            source.remove(argument);
            args[argument.getIndex()] = initialize(argument, input, value);
        }

        for (int i = 0; i < values.size(); i++) {
            final String value = values.get(i);
            String val;
            Argument argument = source.stream().filter(arg -> arg.getNames().contains(value)).findFirst()
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
            source.remove(argument);
            args[argument.getIndex()] = initialize(argument, input, val);
        }

        for (Argument argument : source) {
            if (argument instanceof Optional) {
                args[argument.getIndex()] = initialize(argument, input, "");
            }
        }

        return args;
    }

    protected final Object initialize(Argument argument, String input, String value) throws ParseException {
        try {
            return ArgumentSupport.initialize(argument, value);
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