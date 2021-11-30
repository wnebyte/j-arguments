package com.github.wnebyte.args;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.github.wnebyte.args.util.Strings;

public class PatternCreator implements IPatternCreator {

    /**
     * Variable dictates whether any discovered leading whitespace characters are to be removed for
     * each permutation of the to-be concatenated regular expressions.
     */
    private boolean rmlws = true;

    public void setRmlws(boolean value) {
        this.rmlws = value;
    }

    public String createRegex(final List<Argument> args) {
        return permute(args);
    }

    public Pattern create(final List<Argument> args) {
        return Pattern.compile(permute(args));
    }

    private String permute(final List<Argument> args) {
        // if there are no arguments to permute, return an empty string
        if ((args == null) || (args.size() == 0)) {
            return "";
        }

        String regex  = "(";
        int i = 0;
        // init set of unique permutations
        Set<List<String>> ps = generatePermutations(args);
        for (List<String> e : ps) {
            // as a string
            String s = Arrays.toString(e.toArray());
            s = Strings.removeFirstAndLast(s, '[', ']');
            if (rmlws) {
                s = s.replaceFirst("\\\\s", "");
            }
            // add opening and closing parenthesis around the string
            regex = regex.concat("(").concat(s).concat(")");
            // add '|' regex for every string but the last
            if (i < ps.size() - 1) {
                regex = regex.concat("|");
            }
            i++;
        }
        return regex.replace(", ", "").concat(")");
    }

    private Set<List<String>> generatePermutations(final List<Argument> arguments) {
        // return set
        Set<List<String>> set = new HashSet<>();
        // list of regex's
        LinkedList<String> args = ArgumentSupport.mapToRegexList(arguments, Required.class, Optional.class);
        // positional args
        List<Positional> positional = ArgumentSupport.getInstancesOfSubClass(arguments, Positional.class);
        /*
        if the specified arguments only contain positional arguments,
        map and return them as a set of their regular expressions.
         */
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