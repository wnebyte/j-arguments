package com.github.wnebyte.args;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentUtilTest {

    @Test
    public void test00() {
        ArgumentFactory argumentFactory = new ArgumentFactoryBuilder().build();
        List<Argument> arguments = argumentFactory
                .setNames("-h", "--h")
                .setPositional()
                .create(int.class)
                .setNames("-a", "--a")
                .setRequired()
                .create(int.class)
                .setNames("-b", "--b")
                .setRequired()
                .create(int.class)
                .getArguments();

        List<Positional> pos = ArgumentUtil.getSubclasses(arguments, Positional.class);
        Assert.assertEquals(1, pos.size());
        List<Required> req = ArgumentUtil.getSubclasses(arguments, Required.class);
        Assert.assertEquals(2, req.size());
        List<Optional> opt = ArgumentUtil.getSubclasses(arguments, Optional.class);
        Assert.assertTrue(opt.isEmpty());

        LinkedList<String> regex = ArgumentUtil.getRegularExpressions(arguments, Required.class);
        System.out.println(Arrays.toString(regex.toArray()));
    }
}
