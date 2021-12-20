package com.github.wnebyte.jarguments.parser;

import java.util.*;
import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.exception.TypeConversionException;
import com.github.wnebyte.jarguments.util.Strings;
import static com.github.wnebyte.jarguments.ArgumentSupport.*;

public class ArgumentParser extends AbstractParser<Collection<Argument>> {

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    public ArgumentParser(Collection<Argument> c) {
        super(c);
    }

    /*
    ###########################
    #      UTILITY METHODS    #
    ###########################
    */

    protected static Object initialize(Argument argument, String value, String input) throws ParseException {
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

    /*
    ###########################
    #          METHODS        #
    ###########################
    */

    @Override
    public Object[] parse(String input) throws ParseException {
        Collection<Argument> source = new ArrayList<>(super.source);
        Object[] args = new Object[source.size()];
        Iterator<String> it = split(input).iterator();
        int pos = 0;

        while (it.hasNext()) {
            final String token = it.next();
            String value;
            Argument opt = getByName(source, token);
            opt = (opt != null) ? opt : getByPosition(source, pos++);

            if (opt == null) {
                throw new ParseException(
                        String.format(
                                "No Argument with name/value: %s is left to be parsed.", token
                        )
                );
            }
            else if ((opt instanceof Flag) || (opt instanceof Positional)) {
                value = token;
            }
            else {
                if (!it.hasNext()) {
                    throw new ParseException(
                            String.format(
                                    "Argument with name: %s is missing a value.", token
                            )
                    );
                }
                value = token.concat(" ").concat(it.next());
            }
            source.remove(opt);
            args[opt.getIndex()] = initialize(opt, value, input);
        }

        for (Argument arg : source) {
            if (arg instanceof Optional) {
                args[arg.getIndex()] = initialize(arg, "", input);
            }
        }

        return args;
    }

    @Override
    protected LinkedList<String> split(String input) {
        List<String> c = Strings.split(input, ' ', Arrays.asList('"', '\''));
        return new LinkedList<>(c);
    }
}
