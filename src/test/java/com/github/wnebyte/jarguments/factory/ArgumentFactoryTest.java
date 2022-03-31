package com.github.wnebyte.jarguments.factory;

import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.Assert;
import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.convert.TypeConverterMap;
import com.github.wnebyte.jarguments.exception.ParseException;
import static com.github.wnebyte.jarguments.ArgumentSupport.*;

public class ArgumentFactoryTest {

    @Test
    public void test00() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a", "--a")
                .setDescription("test arg")
                .isRequired()
                .setType(float.class)
                .append()
                .get();
        Argument arg = getByName(arguments, "-a");
        Assert.assertEquals("-a", arg.getNames().toArray()[0]);
        Assert.assertEquals("--a", arg.getNames().toArray()[1]);
        Assert.assertTrue(arg instanceof Required);
        Assert.assertEquals(float.class, arg.getType());
        Assert.assertEquals("test arg", arg.getDescription());
        Assert.assertEquals(0, arg.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppend() {
        List<Argument> arguments = new ArgumentFactoryBuilder()
                .useTypeConverterMap(TypeConverterMap.getInstance())
                .build()
                .isPositional()
                .setType(UUID.class) // should throw the expected exception
                .append()
                .setName("bar")
                .setType(String.class)
                .append()
                .setName("b")
                .isOptional()
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
                .isRequired()
                .append(int.class)
                .setName("-c")
                .isOptional()
                .setType(String.class)
                .append()
                .get();
        Assert.assertTrue(getByName(arguments, "-a") instanceof Flag);
        Assert.assertTrue(getByName(arguments, "-b") instanceof Required);
        Assert.assertTrue(getByName(arguments, "-c") instanceof Optional);
        boolean bool = (boolean) ArgumentSupport.initialize(getByName(arguments, "-a"), "");
        Assert.assertFalse(bool);
        bool = (boolean) ArgumentSupport.initialize(getByName(arguments, "-a"), "-a");
        Assert.assertTrue(bool);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test03() {
        List<Argument> c = ArgumentFactory.builder().build()
                .setName("--name")
                .append(int.class)
                .setName("test")
                .append(int.class)
                .setName("--name")
                .append(int.class)
                .get();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test04() {
        List<Argument> c = ArgumentFactory.builder().build()
                .setName("a", "--name")
                .append(int.class)
                .setName("test")
                .append(int.class)
                .setName("--a", "--name")
                .append(int.class)
                .get();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test05() {
        List<Argument> c = ArgumentFactory.builder().build()
                .setName("-a")
                .isOptional()
                .setDefaultValue("hello")
                .setType(int.class)
                .setTypeConverter(TypeConverterMap.getInstance().INTEGER_TYPE_CONVERTER)
                .append()
                .get();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test06() {
        List<Argument> c = ArgumentFactory.builder().build()
                .setName("-a")
                .isFlag()
                .setDefaultValue("test")
                .setFlagValue("10")
                .setType(int.class)
                .setTypeConverter(TypeConverterMap.getInstance().INTEGER_TYPE_CONVERTER)
                .append()
                .get();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test07() {
        List<Argument> c = ArgumentFactory.builder().build()
                .setName("-a")
                .isFlag()
                .setDefaultValue("10")
                .setFlagValue("test")
                .setType(int.class)
                .setTypeConverter(TypeConverterMap.getInstance().INTEGER_TYPE_CONVERTER)
                .append()
                .get();
    }

}