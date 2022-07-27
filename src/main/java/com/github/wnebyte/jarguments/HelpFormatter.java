package com.github.wnebyte.jarguments;

import java.util.*;
import java.util.stream.Collectors;
import com.github.wnebyte.jarguments.util.Chars;
import com.github.wnebyte.jarguments.util.Sets;
import com.github.wnebyte.jarguments.util.Strings;
import static com.github.wnebyte.jarguments.ArgumentSupport.getArguments;
import static com.github.wnebyte.jarguments.util.Objects.requireNonNullElseGet;

public class HelpFormatter implements Formatter<ContextView> {

    /*
    ###########################
    #      STATIC METHODS     #
    ###########################
    */

    public static HelpFormatter get() {
        if (INSTANCE == null) {
            INSTANCE = new HelpFormatter();
        }
        return INSTANCE;
    }

    private static int maxLength(Collection<Argument> c, Formatter<Argument> f) {
        return c.stream().map(f)
                .max(Comparator.comparingInt(String::length))
                .orElse(Strings.EMPTY)
                .length();
    }

    private static String indent(int len) {
        char[] arr = new char[len];
        Arrays.fill(arr, Chars.WHITESPACE);
        return new String(arr);
    }

    /*
    ###########################
    #      STATIC FIELDS      #
    ###########################
    */

    private static HelpFormatter INSTANCE = null;

    /*
    ###########################
    #          FIELDS         #
    ###########################
    */

    protected Formatter<Required> REQUIRED_USAGE_FORMATTER
            = new Formatter<Required>() {
        @Override
        public String apply(Required argument) {
            if (argument.hasMetavar()) {
                return argument.getCanonicalName()
                        .concat(Strings.WHITESPACE)
                        .concat(argument.getMetavar());
            }
            else if (argument.hasChoices()) {
                return argument.getCanonicalName()
                        .concat(Strings.WHITESPACE)
                        .concat(Sets.toString(argument.getChoices()));
            }
            else {
                return argument.getCanonicalName()
                        .concat(Strings.WHITESPACE)
                        .concat(argument.getType().getSimpleName());
            }
        }
    };

    protected Formatter<Required> REQUIRED_DETAILS_FORMATTER
            = new Formatter<Required>() {
        @Override
        public String apply(Required argument) {
            return String.join(", ", argument.getNames());
        }
    };

    protected Formatter<Positional> POSITIONAL_USAGE_FORMATTER
            = new Formatter<Positional>() {
        @Override
        public String apply(Positional argument) {
            if (argument.hasMetavar()) {
                return argument.getMetavar();
            }
            else if (argument.hasChoices()) {
                return Sets.toString(argument.getChoices());
            }
            else {
                return argument.getType().getSimpleName();
            }
        }
    };

    protected Formatter<Positional> POSITIONAL_DETAILS_FORMATTER
            = new Formatter<Positional>() {
        @Override
        public String apply(Positional argument) {
            if (argument.hasMetavar()) {
                return argument.getMetavar();
            }
            else {
                return argument.getType().getSimpleName();
            }
        }
    };

    protected Formatter<Optional> OPTIONAL_USAGE_FORMATTER
            = new Formatter<Optional>() {
        @Override
        public String apply(Optional argument) {
            if (argument.hasMetavar()) {
                return argument.getCanonicalName()
                        .concat(Strings.WHITESPACE)
                        .concat(argument.getMetavar());
            }
            else if (argument.hasChoices()) {
                return argument.getCanonicalName()
                        .concat(Strings.WHITESPACE)
                        .concat(Sets.toString(argument.getChoices()));
            }
            else if (argument.hasDefaultValue()) {
                return argument.getCanonicalName()
                        .concat(Strings.WHITESPACE)
                        .concat(argument.getDefaultValue());
            }
            else {
                return argument.getCanonicalName()
                        .concat(Strings.WHITESPACE)
                        .concat(argument.getType().getSimpleName());
            }
        }
    };

    protected Formatter<Optional> OPTIONAL_DETAILS_FORMATTER
            = new Formatter<Optional>() {
        @Override
        public String apply(Optional argument) {
            return String.join(", ", argument.getNames());
        }
    };

    protected Formatter<Flag> FLAG_USAGE_FORMATTER
            = new Formatter<Flag>() {
        @Override
        public String apply(Flag argument) {
            return argument.getCanonicalName();
        }
    };

    protected Formatter<Flag> FLAG_DETAILS_FORMATTER
            = new Formatter<Flag>() {
        @Override
        public String apply(Flag argument) {
            return String.join(", ", argument.getNames());
        }
    };

    protected Formatter<Argument> ARGUMENT_USAGE_FORMATTER
            = new Formatter<Argument>() {
        @Override
        public String apply(Argument argument) {
            Class<? extends Argument> cls = argument.getClass();

            if (cls == Required.class) {
                return REQUIRED_USAGE_FORMATTER.apply((Required)argument);
            }
            if (cls == Positional.class) {
                return POSITIONAL_USAGE_FORMATTER.apply((Positional)argument);
            }
            if (cls == Optional.class) {
                return "[" + OPTIONAL_USAGE_FORMATTER.apply((Optional)argument) + "]";
            }
            if (cls == Flag.class) {
                return "[" + FLAG_USAGE_FORMATTER.apply((Flag)argument) + "]";
            }

            return "";
        }
    };

    protected Formatter<Argument> ARGUMENT_DETAILS_FORMATTER
            = new Formatter<Argument>() {
        @Override
        public String apply(Argument argument) {
            Class<? extends Argument> cls = argument.getClass();

            if (cls == Required.class) {
                return REQUIRED_DETAILS_FORMATTER.apply((Required)argument);
            }
            if (cls == Positional.class) {
                return POSITIONAL_DETAILS_FORMATTER.apply((Positional)argument);
            }
            if (cls == Optional.class) {
                return OPTIONAL_DETAILS_FORMATTER.apply((Optional)argument);
            }
            if (cls == Flag.class) {
                return FLAG_DETAILS_FORMATTER.apply((Flag)argument);
            }

            return "";
        }
    };

    protected Formatter<Collection<Argument>> COLLECTION_USAGE_FORMATTER
            = new Formatter<Collection<Argument>>() {
        @Override
        public String apply(Collection<Argument> c) {
            return c.stream().map(ARGUMENT_USAGE_FORMATTER)
                    .collect(Collectors.joining(Strings.WHITESPACE));
        }
    };

    /*
    ###########################
    #          METHODS        #
    ###########################
    */

    @Override
    public String apply(ContextView context) {
        int maxLength = maxLength(context.getArguments(), ARGUMENT_DETAILS_FORMATTER);
        StringBuilder out = new StringBuilder();
        out.append("Usage: ");
        if (context.hasName()) {
            out.append(context.getName()).append(Strings.WHITESPACE);
        }
        out.append(COLLECTION_USAGE_FORMATTER.apply(context.getArguments()))
                .append("\n\n");
        if (context.hasDescription()) {
            out.append(context.getDescription()).append("\n\n");
        }

        Collection<Required> req = getArguments(context.getArguments(), Required.class);
        if (!req.isEmpty()) {
            out.append("Required Arguments: ")
                    .append("\n");
            for (Required argument : req) {
                String s = format(argument, ARGUMENT_DETAILS_FORMATTER, maxLength);
                out.append(s);
                out.append("\n");
            }
        }

        Collection<Optional> opt = getArguments(context.getArguments(), Optional.class);
        if (!opt.isEmpty()) {
            if (!req.isEmpty()) {
                out.append("\n");
            }
            out.append("Optional Arguments: ")
                    .append("\n");
            for (Optional argument : opt) {
                String s = format(argument, ARGUMENT_DETAILS_FORMATTER, maxLength);
                out.append(s);
                out.append("\n");
            }
        }

        return out.toString();
    }

    protected String format(Argument argument, Formatter<Argument> formatter, int maxLength) {
        String s = ARGUMENT_DETAILS_FORMATTER.apply(argument);
        String indent = indent((maxLength + 1) - s.length());
        return new StringBuilder().append("\t").append(s).append(indent)
                .append(requireNonNullElseGet(argument.getDescription(), () -> Strings.EMPTY)).toString();
    }
}
