package com.github.wnebyte.jarguments.val;

import java.util.*;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.Flag;
import com.github.wnebyte.jarguments.Optional;
import com.github.wnebyte.jarguments.Positional;
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
            Argument opt = getByName(source, token);
            opt = (opt == null) ? getByPosition(source, pos++) : opt;

            if (opt == null) {
                return false;
            }
            else if ((opt instanceof Flag) || (opt instanceof Positional)) {
                value = token;
            }
            else {
                if (!it.hasNext()) {
                    return false;
                }
                value = token.concat(" ").concat(it.next());
            }

            boolean match = opt.getPattern().matcher(" ".concat(value)).matches();
            if (!match) {
                return false;
            }

            source.remove(opt);
        }

        for (Argument argument : source) {
            if (!(argument instanceof Optional)) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected LinkedList<String> split(String input) {
        List<String> c = Strings.split(input, ' ', Arrays.asList('\'', '"'));
        return new LinkedList<>(c);
    }
}
