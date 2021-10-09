package com.github.wnebyte.args;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.github.wnebyte.args.util.Strings;

public class PatternCreator {

    public String createRegex(final List<Argument> arguments, final boolean rmLws) {
        return permute(arguments, rmLws);
    }

    public Pattern createPattern(final List<Argument> arguments, final boolean rmLws) {
        return Pattern.compile(permute(arguments, rmLws));
    }

    private String permute(final List<Argument> arguments, final boolean rmLws) {
        // if there are no arguments to permute, return an empty string
        if ((arguments == null) || (arguments.size() == 0)) {
            return "";
        }

        String regex  = "(";
        int i = 0;
        // init set of unique permutations
        Set<List<String>> powerSet = powerSet(arguments);
        for (List<String> e : powerSet) {
            // as a string
            String s = Arrays.toString(e.toArray());
            s = Strings.removeFirstAndLast(s, '[', ']');
            if (rmLws) {
                s = s.replaceFirst("\\\\s", "");
            }
            // add opening and closing parenthesis around the string
            regex = regex.concat("(").concat(s).concat(")");
            // add '|' regex for every string but the last
            if (i < powerSet.size() - 1) {
                regex = regex.concat("|");
            }
            i++;
        }
        return regex.replace(", ", "").concat(")");
    }

    private Set<List<String>> powerSet(final List<Argument> arguments) {
        Set<List<String>> set = new HashSet<>();
        // the required & optional argument's regular expressions
        LinkedList<String> args = ArgumentUtil.getRegularExpressions(arguments, Required.class, Optional.class);
        // the positional arguments
        List<Positional> positional = ArgumentUtil.getSubclasses(arguments, Positional.class);
        // if the specified arguments only contain positional arguments,
        // map and return them as a set of their regular expressions.
        if (args.isEmpty()) {
            set.add(positional.stream().map(Argument::getRegex).collect(Collectors.toList()));
            return set;
        }

        // iterate over the non-positional argument regex's
        for (int i = 0; i < args.size(); i++) {
            // shift the regex linked list to the left <<
            String head = args.pop();
            args.add(head);
            // init a mirror list of the shifted linked list consisting of non-positional arguments.
            List<String> mirror = new ArrayList<>(args);

            // iterate over the mirror list
            for (int j = 0; j < mirror.size(); j++) {
                if (j != 0) {
                    // swap the elements of the mirror list around
                    Collections.swap(mirror, j, j - 1);
                }
                // for each permutation of the mirror list, insert back each positional argument
                // at its fixed positions.
                set.add(new ArrayList<String>(mirror) {{
                    arguments.stream().filter(arg -> (arg instanceof Positional))
                            .forEach(arg -> {
                                Positional argument = (Positional) arg;
                                add(argument.getPosition(), argument.getRegex());
                            });
                }});
            }
        }
        return set;
    }
}