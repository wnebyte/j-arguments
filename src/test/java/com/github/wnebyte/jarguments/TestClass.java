package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.converter.TypeConverterMap;
import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactory;
import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactoryBuilder;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class TestClass {

    @Test
    public void test00() {
        ArgumentCollectionFactory argumentCollectionFactory = new ArgumentCollectionFactoryBuilder()
                .excludeCharacters(Arrays.asList(' ', '"', '\''))
                .useTypeConverterMap(TypeConverterMap.getInstance())
                .build();

        List<Argument> args = argumentCollectionFactory
                .setNames("-h", "--help")
                .setIsRequired()
                .append(int.class)
                .setNames("-a", "--a")
                .setIsOptional()
                .setDefaultValue("10")
                .setDescription("foo bar ...")
                .append(Integer.class)
                .get();

        args.forEach(System.out::println);
    }

}
