package com.github.wnebyte.args;

import com.github.wnebyte.args.factory.ArgumentFactory;
import com.github.wnebyte.args.factory.ArgumentFactoryBuilder;
import org.junit.Test;

import java.util.List;

public class PatternCreatorTest {

    @Test
    public void test00() {
        ArgumentFactory argumentFactory = new ArgumentFactoryBuilder().build();
        List<Argument> arguments = argumentFactory
                .setNames("-h", "--h")
                .isPositional()
                .create(int.class)
                .setNames("-a", "--a")
                .isRequired()
                .create(int.class)
                .setNames("-b", "--b")
                .isRequired()
                .create(int.class)
                .getArguments();

    }
}
