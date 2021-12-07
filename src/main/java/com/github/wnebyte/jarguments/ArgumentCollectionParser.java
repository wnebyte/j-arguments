package com.github.wnebyte.jarguments;

import java.util.*;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Collections;
import com.github.wnebyte.jarguments.util.Strings;

public class ArgumentCollectionParser extends AbstractParser<Collection<Argument>> {

    public ArgumentCollectionParser(Collection<Argument> source) {
        super(new ArrayList<>(source));
    }

    @Override
    public Object[] parse(String input) throws ParseException {
        Object[] args = new Object[super.getSource().size()];
        List<Positional> positionalArgs = ArgumentSupport
                .getInstancesOfSubClass(super.getSource(), Positional.class);
        LinkedList<String> values = split(input);

        // iterate and initialize any positional arguments first.
        for (Positional arg : positionalArgs) {
            String value = values.pop();
            super.getSource().remove(arg);
            args[arg.getIndex()] = initialize(input, arg, value);
        }

        // iterate and initialize any 'named' arguments next.
        for (int i = 0; i < values.size(); i++) {
            final String value = values.get(i);
            String val;
            Argument arg = ArgumentSupport.getByName(super.getSource(), value);
            if (arg == null) {
                throw new ParseException(
                        "No Argument with name: '" + value + "' exists."
                );
            }
            // get the next element if one exists.
            String nextVal = Collections.getNextElement(values, i++, null);
            if (nextVal != null) {
                val = value.concat(" ").concat(nextVal);
            } else {
                val = value;
            }
            super.getSource().remove(arg);
            args[arg.getIndex()] = initialize(input, arg, val);
        }

        // initialize any remaining optional arguments.
        for (Argument arg : super.getSource()) {
            if (arg instanceof Optional) {
                String val = "";
                args[arg.getIndex()] = initialize(input, arg, val);
            }
        }

        return args;
    }

    protected Object initialize(String input, Argument arg, String value) throws ParseException {
        try {
            return arg.initialize(value);
        } catch (ParseException e) {
            e.setArgument(arg);
            e.setInput(input);
            e.setValue(value);
            throw e;
        }
    }

    @Override
    protected LinkedList<String> split(String input) {
        List<String> c = Strings.split(input, ' ', Arrays.asList('"', '\''));
        return new LinkedList<>(c);
    }
}