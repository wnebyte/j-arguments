package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.util.TokenSequence;
import org.junit.Test;
import org.junit.Assert;

public class TokenSequenceTest {

    @Test
    public void test00() {
        TokenSequence tokens = TokenSequence.tokenize("hello [1,2,3,4] aye");
        Assert.assertEquals(1, tokens.subTokens(2, tokens.size()).size());
        Assert.assertEquals(2, tokens.subTokens(1, tokens.size()).size());
    }
}
