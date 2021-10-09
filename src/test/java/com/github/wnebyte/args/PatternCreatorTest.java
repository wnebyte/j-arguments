package com.github.wnebyte.args;

import org.junit.Test;

import java.util.List;

public class PatternCreatorTest {

    @Test
    public void test00() {
        ArgumentFactory argumentFactory = new ArgumentFactoryBuilder().build();
        List<Argument> arguments = argumentFactory
                .setNames("-h", "--h")
                .setPositional()
                .create(int.class)
                .setNames("-a", "--a")
                .setRequired()
                .create(int.class)
                .setNames("-b", "--b")
                .setRequired()
                .create(int.class)
                .getArguments();

    }
}
