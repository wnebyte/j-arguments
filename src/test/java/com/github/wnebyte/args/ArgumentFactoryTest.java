package com.github.wnebyte.args;

import com.github.wnebyte.args.factory.ArgumentFactoryBuilder;
import org.junit.Test;
import java.util.List;

public class ArgumentFactoryTest {

    @Test
    public void test00() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setNames("-a", "--a")
                .setDescription("test arg")
                .isRequired()
                .setType(float.class)
                .create()
                .getArguments();
    }

    @Test
    public void test01() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setNames("-a")
                .setType(String.class)
                .isOptional()
                .setDefaultValue("def 0")
                .create()
                .getArguments();
    }
}