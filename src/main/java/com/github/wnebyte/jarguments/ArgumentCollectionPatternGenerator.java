package com.github.wnebyte.jarguments;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.github.wnebyte.jarguments.util.Strings;

public class ArgumentCollectionPatternGenerator extends AbstractPatternGenerator<Collection<Argument>> {

    /**
     * Variable dictates whether any discovered leading whitespace characters are to be removed for
     * each permutation of the to-be concatenated regular expressions.
     */
    private boolean rmlws = true;

    /**
     * Variable dictates whether a '^' character is to be inserted at the start of the generated regex.
     */
    private boolean inclSol = true;

    /**
     * Variable dictates whether a '$' character is to be appended at the end of the generated regex.
     */
    private boolean inclEol = true;

    public ArgumentCollectionPatternGenerator(final Collection<Argument> args) {
        super(args);
    }

    /**
     * @param value whether leading whitespace chars are to be removed for each permutation.
     */
    public void setRmlws(boolean value) {
        this.rmlws = value;
    }

    /**
     * @param value whether the regex should start with a '^' char.
     */
    public void setInclSol(boolean value) {
        this.inclSol = value;
    }

    /**
     * @param value whether the regex should end with a '$' char.
     */
    public void setInclEol(boolean value) {
        this.inclEol = value;
    }

    public String generateRegex() {
        String body = permute(super.getSource());
        if (inclSol) {
            body = "^".concat(body);
        }
        if (inclEol) {
            body = body.concat("$");
        }
        return body;
    }

    public Pattern generatePattern() {
        String regex = generateRegex();
        return Pattern.compile(regex);
    }

    private String permute(final Collection<Argument> args) {
        // if there are no arguments to permute, return an empty string
        if ((args == null) || (args.isEmpty())) {
            return "";
        }

        String regex  = "(";
        int i = 0;
        // generate set of permutations
        Set<List<String>> set = generatePermutations(args);
        for (List<String> element : set) {
            String s = Arrays.toString(element.toArray());
            // normalization
            s = Strings.removeFirstAndLast(s, '[', ']');
            if (rmlws) {
                // looks weird, but works.
                s = s.replaceFirst("\\\\s", "");
            }
            // add opening and closing brackets around the string
            regex = regex.concat("(").concat(s).concat(")");
            // add '|' char for every permutation but the last
            if (i < set.size() - 1) {
                regex = regex.concat("|");
            }
            i++;
        }
        return regex.replace(", ", "").concat(")");
    }

    private Set<List<String>> generatePermutations(final Collection<Argument> arguments) {
        Set<List<String>> set = new HashSet<>();
        LinkedList<String> args = ArgumentSupport
                .mapToRegexList(arguments, (arg) -> !(arg instanceof Positional));
        List<Positional> positional = ArgumentSupport.getInstancesOfSubClass(arguments, Positional.class);

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