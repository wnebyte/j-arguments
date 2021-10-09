package com.github.wnebyte.args;

import com.github.wnebyte.args.exception.ConstraintException;
import com.github.wnebyte.args.exception.ParseException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentParserTest {

    @Test
    public void test00() throws ParseException, ConstraintException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setNames("-a", "--a", "---a")
                .setType(String.class)
                .setOptional()
                .setDefaultValue("def0")
                .create()
                .setNames("-b", "--b", "---b")
                .setOptional()
                .setDefaultValue("def1")
                .setType(String.class)
                .create()
                .getArguments();
        Object[] args = new ArgumentParser(arguments).parse("");
    }

    @Test
    public void test01() {
        ArgumentParser parser = new ArgumentParser(new ArrayList<>());
        List<String> list = parser.split("");
        System.out.println("size: " + list.size() + Arrays.toString(list.toArray()));
    }
}
