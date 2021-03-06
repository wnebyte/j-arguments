package com.github.wnebyte.jarguments.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class TokenSequence implements Iterable<String> {

    /*
    ###########################
    #      STATIC METHODS     #
    ###########################
    */

    public static TokenSequence tokenize(String input) {
        List<String> tokens = Strings.split(input, Chars.WHITESPACE, Chars.QUOTATION_CHARACTERS);
        return new TokenSequence(tokens);
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

    private TokenSequence(List<String> tokens) {
        this.tokens = tokens;
    }

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    public TokenSequence subTokens(int fromIndex, int toIndex) {
        return new TokenSequence(new ArrayList<>(tokens.subList(fromIndex, toIndex)));
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
