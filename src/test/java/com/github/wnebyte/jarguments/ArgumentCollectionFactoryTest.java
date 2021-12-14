package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.converter.TypeConverterMap;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.UUID;

public class ArgumentCollectionFactoryTest {

    @Test
    public void test00() {
        List<Argument> arguments = new ArgumentCollectionFactoryBuilder().build()
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
        List<Argument> arguments = new ArgumentCollectionFactoryBuilder()
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
        List<Argument> args = new ArgumentCollectionFactoryBuilder().build()
                .setName("$")
                .append(int.class)
                .get();
    }

    @Test
    public void test02() throws ParseException {
        List<Argument> args = new ArgumentCollectionFactoryBuilder().build()
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
        System.out.println(args.get(0).getClass());
        Assert.assertTrue(args.get(0) instanceof Flag);
        Assert.assertTrue(args.get(1) instanceof Required);
        Assert.assertTrue(args.get(2) instanceof Optional);
        boolean bool = (boolean) args.get(0).initialize("");
        Assert.assertFalse(bool);
        bool = (boolean) args.get(0).initialize("-a");
        Assert.assertTrue(bool);
    }
}