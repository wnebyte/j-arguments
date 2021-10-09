package com.github.wnebyte.args;

import org.junit.Test;
import java.util.List;

public class ArgumentFactoryTest {

    @Test
    public void test00() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setNames("-a", "--a")
                .setDescription("test arg")
                .setRequired()
                .setType(float.class)
                .create()
                .getArguments();
    }

    @Test
    public void test01() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setNames("-a")
                .setType(String.class)
                .setOptional()
                .setDefaultValue("def 0")
                .create()
                .getArguments();
    }
}