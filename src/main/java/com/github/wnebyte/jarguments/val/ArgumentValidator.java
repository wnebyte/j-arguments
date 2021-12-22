package com.github.wnebyte.jarguments.val;

import java.util.*;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.Flag;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.Positional;
import com.github.wnebyte.jarguments.exception.MissingArgumentException;
import com.github.wnebyte.jarguments.exception.MissingArgumentValueException;
import com.github.wnebyte.jarguments.exception.NoSuchArgumentException;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.Strings;
import static com.github.wnebyte.jarguments.ArgumentSupport.*;

public class ArgumentValidator extends AbstractValidator<Collection<Argument>> {

    public ArgumentValidator(Collection<Argument> c) {
        super(c);
    }

    @Override
    public boolean validate(String input) {
        List<Argument> source = new ArrayList<>(super.source);
        Iterator<String> it = split(input).iterator();
        int pos = 0;

        while (it.hasNext()) {
            final String token = it.next();
            String value;
            Argument arg = getByName(source, token);
            arg = (arg == null) ? getByPosition(source, pos++) : arg;

            if (arg == null) {
                return false;
            }
            else if ((arg instanceof Flag) || (arg instanceof Positional)) {
                value = token;
            }
            else {
                if (!it.hasNext()) {
                    return false;
                }
                value = token.concat(" ").concat(it.next());
            }

            boolean match = arg.getPattern().matcher(" ".concat(value)).matches();
            if (!match) {
                return false;
            }

            source.remove(arg);
        }

        for (Argument argument : source) {
            if (!(argument instanceof Optional)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean matches(String input) throws ParseException {
        List<Argument> source = new ArrayList<>(this.source);
        List<String> tokens = split(input);
        Iterator<String> it = tokens.iterator();
        int pos = 0;

        while (it.hasNext()) {
            final String token = it.next();
            String value;
            Argument arg = getByName(source, token);
            arg = (arg == null) ? getByPosition(source, pos++) : arg;

            if (arg == null) {
                throw new NoSuchArgumentException(
                        String.format(
                                "Argument with name/value: '%s' does not exist, or has already been parsed.", token
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
                                    "Argument: '%s' is missing a value.", token
                            ), input, arg
                    );
                }
                value = token.concat(" ").concat(it.next());
            }

            boolean match = arg.getPattern().matcher(" ".concat(value)).matches();
            if (!match) {
                return false;
            }

            source.remove(arg);

        }

        List<Argument> missing = getMissingArguments(source);
        if (!missing.isEmpty()) {
            throw new MissingArgumentException(
                    String.format("Argument: '%s' is missing.",
                            missing.get(0).toString()), input, missing.get(0)
            );
        }

        return true;
    }

    private List<Argument> getMissingArguments(List<Argument> source) {
        List<Argument> missing = new ArrayList<>();
        if ((source == null) || (source.isEmpty()))
            return missing;
        for (Argument arg : source) {
            if (!(arg instanceof Optional)) {
                missing.add(arg);
            }
        }
        return missing;
    }

    @Override
    protected LinkedList<String> split(String input) {
        List<String> c = Strings.split(input, ' ', Arrays.asList('\'', '"'));
        return new LinkedList<>(c);
    }
}
