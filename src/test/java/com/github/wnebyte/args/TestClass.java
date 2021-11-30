package com.github.wnebyte.args;

import com.github.wnebyte.args.converter.TypeConverterMap;
import com.github.wnebyte.args.factory.ArgumentFactory;
import com.github.wnebyte.args.factory.ArgumentFactoryBuilder;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class TestClass {

    @Test
    public void test00() {
        ArgumentFactory argumentFactory = new ArgumentFactoryBuilder()
                .excludeCharacters(Arrays.asList(' ', '"', '\''))
                .useTypeConverterMap(TypeConverterMap.getInstance())
                .build();

        List<Argument> args = argumentFactory
                .setNames("-h", "--help")
                .isRequired()
                .create(int.class)
                .setNames("-a", "--a")
                .isOptional()
                .setDefaultValue("10")
                .setDescription("foo bar ...")
                .create(Integer.class)
                .getArguments();

        args.forEach(System.out::println);
    }

}
