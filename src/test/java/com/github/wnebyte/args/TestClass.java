package com.github.wnebyte.args;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class TestClass {

    @Test
    public void test00() {
        ArgumentFactory argumentFactory = new ArgumentFactoryBuilder()
                .exclude(Arrays.asList(' ', '"', '\''))
                .useTypeConverters(TypeConverters.getInstance())
                .build();

        List<Argument> args = argumentFactory
                .setNames("-h", "--help")
                .setRequired()
                .create(int.class)
                .setNames("-a", "--a")
                .setOptional()
                .setDefaultValue("10")
                .setDescription("foo bar ...")
                .create(Integer.class)
                .getArguments();

        args.forEach(System.out::println);
    }

}
