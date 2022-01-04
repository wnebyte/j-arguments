package com.github.wnebyte.jarguments;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import com.github.wnebyte.jarguments.util.Chars;
import com.github.wnebyte.jarguments.util.Strings;

public class Tokens implements Iterable<String> {

    /*
    ###########################
    #      STATIC METHODS     #
    ###########################
    */

    public static Tokens tokenize(String input) {
        List<String> tokens = Strings.split(input, Chars.WHITESPACE, Chars.QUOTATION_SINGLE, Chars.QUOTATION_DOUBLE);
        return new Tokens(tokens);
    }

    /*
    ###########################
    #          FIELDS         #
    ###########################
    */

    private final List<String> tokens;

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    private Tokens(List<String> tokens) {
        this.tokens = tokens;
    }

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    public Tokens subTokens(int fromIndex, int toIndex) {
        return new Tokens(new ArrayList<>(tokens.subList(fromIndex, toIndex)));
    }

    public String join() {
        return String.join(Strings.WHITESPACE, tokens);
    }

    public int size() {
        return tokens.size();
    }

    public String get(int index) {
        return tokens.get(index);
    }

    public boolean contains(String token) {
        return tokens.contains(token);
    }

    @Override
    public Iterator<String> iterator() {
        return tokens.iterator();
    }

    @Override
    public String toString() {
        return Arrays.toString(tokens.toArray());
    }
}
