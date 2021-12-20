package com.github.wnebyte.jarguments.pattern;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.Positional;
import com.github.wnebyte.jarguments.util.Strings;

public class ArgumentPatternGenerator extends AbstractPatternGenerator<Collection<Argument>> {

    private Set<List<Argument>> set;

    private boolean rmlws = true;

    private boolean inclSol = true;

    private boolean inclEol = true;

    public ArgumentPatternGenerator(Collection<Argument> c) {
        super(c);
    }

    public ArgumentPatternGenerator() {
        this(new ArrayList<>());
    }

    @Override
    public String getRegex() {
        String content = permute(super.getSource());
        if (inclSol) {
            content = "^".concat(content);
        }
        if (inclEol) {
            content = content.concat("$");
        }
        return content;
    }

    @Override
    public Pattern getPattern() {
        String regex = getRegex();
        return Pattern.compile(regex);
    }

    protected Set<List<String>> getPermutations(Collection<Argument> c) {
        set = new HashSet<>();
        List<Argument> copy = new ArrayList<>(c);
        heap(copy.size(), copy);
        return set.stream().map(args -> args.stream()
                .map(Argument::getRegex)
                .collect(Collectors.toList()))
                .collect(Collectors.toSet());
    }

    protected void heap(int n, List<Argument> c) {
        if (n == 1) {
            save(new ArrayList<>(c));
        } else {
            for (int i = 0; i < n - 1; i++) {
                heap(n - 1, c);
                if (n % 2 == 0) {
                    Collections.swap(c, i, n - 1);
                } else {
                    Collections.swap(c, 0, n - 1);
                }
            }
            heap(n - 1, c);
        }
    }

    private void save(List<Argument> c) {
        sort(c);
        set.add(c);
    }

    protected void sort(List<Argument> c) {
        if (c.isEmpty())
            return;

        List<Integer> indices = new ArrayList<>();
        List<Positional> objects = new ArrayList<>();

        for (int i = 0; i < c.size(); i++) {
            Argument e = c.get(i);
            if (e instanceof Positional) {
                indices.add(i);
                objects.add((Positional) e);
            }
        }

        objects.sort(Positional::compareTo);

        for (int i = 0; i < objects.size(); i++) {
            c.set(indices.get(i), objects.get(i));
        }
    }

    protected final String permute(Collection<Argument> c) {
        if ((c == null) || (c.isEmpty()))
            return "";

        String regex  = "(";
        int i = 0;
        Set<List<String>> set = getPermutations(c);

        for (List<String> element : set) {
            String toString = Arrays.toString(element.toArray());
            // normalization
            toString = Strings.removeFirstAndLast(toString, '[', ']');
            if (rmlws) {
                toString = toString.replaceFirst("\\\\s", "");
            }
            regex = regex.concat("(").concat(toString).concat(")");
            // add '|' char for every permutation but the last
            if (i < set.size() - 1) {
                regex = regex.concat("|");
            }
            i++;
        }

        return regex.replace(", ", "").concat(")");
    }

    public void setRmlws(boolean value) {
        this.rmlws = value;
    }

    public void setInclSol(boolean value) {
        this.inclSol = value;
    }

    public void setInclEol(boolean value) {
        this.inclEol = true;
    }
}
