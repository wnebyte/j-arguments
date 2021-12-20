package com.github.wnebyte.jarguments.factory;

import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.convert.TypeConverterMap;
import com.github.wnebyte.jarguments.exception.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArgumentFactoryTest {

    private static class OptionsBuilderBuilder {

        public List<Argument> get() {
            return new ArrayList<>();
        }
    }

    @Test
    public void testName() {
        List<Argument> c = new OptionsBuilderBuilder()
                .get();
    }

    @Test
    public void test00() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a", "--a")
                .setDescription("test arg")
                .setIsRequired()
                .setType(float.class)
                .append()
                .get();
        Argument arg = arguments.get(0);
        Assert.assertEquals("-a", arg.getNames().toArray()[0]);
        Assert.assertEquals("--a", arg.getNames().toArray()[1]);
        Assert.assertTrue(arg instanceof Required);
        Assert.assertEquals(float.class, arg.getType());
        Assert.assertEquals("test arg", arg.getDesc());
        Assert.assertEquals(0, arg.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppend() {
        List<Argument> arguments = new ArgumentFactoryBuilder()
                .useTypeConverterMap(TypeConverterMap.getInstance())
                .build()
                .setIsPositional()
                .setType(UUID.class) // should throw the expected exception
                .append()
                .setName("bar")
                .setType(String.class)
                .append()
                .setName("b")
                .setIsOptional()
                .setType(int.class)
                .append()
                .get();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test01() {
        List<Argument> args = new ArgumentFactoryBuilder().build()
                .setName("$")
                .append(int.class)
                .get();
    }

    @Test
    public void test02() throws ParseException {
        List<Argument> arguments = ArgumentFactory.builder().build()
                .setName("-a")
                .append(boolean.class)
                .setName("-b")
                .setIsRequired()
                .append(int.class)
                .setName("-c")
                .setIsOptional()
                .setType(String.class)
                .append()
                .get();
        System.out.println(arguments.get(0).getClass());
        Assert.assertTrue(arguments.get(0) instanceof Flag);
        Assert.assertTrue(arguments.get(1) instanceof Required);
        Assert.assertTrue(arguments.get(2) instanceof Optional);
        boolean bool = (boolean) ArgumentSupport.initialize(arguments.get(0), "");
        Assert.assertFalse(bool);
        bool = (boolean) ArgumentSupport.initialize(arguments.get(0), "-a");
        Assert.assertTrue(bool);
    }

    @Test
    public void test03() {

    }


}