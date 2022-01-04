package com.github.wnebyte.jarguments;

import org.junit.Test;


public class TokensTest {

    @Test
    public void test00() {
        Tokens tokens = Tokens.tokenize("hello [1,2,3,4] aye");
        System.out.println(tokens.subTokens(2, tokens.size()));
        System.out.println(tokens.subTokens(1, tokens.size()));

    }
}
