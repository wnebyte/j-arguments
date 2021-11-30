package com.github.wnebyte.args;

import com.github.wnebyte.args.factory.ArgumentFactory;
import com.github.wnebyte.args.factory.ArgumentFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentSupportTest {

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

        List<Positional> pos = ArgumentSupport.getInstancesOfSubClass(arguments, Positional.class);
        Assert.assertEquals(1, pos.size());
        List<Required> req = ArgumentSupport.getInstancesOfSubClass(arguments, Required.class);
        Assert.assertEquals(2, req.size());
        List<Optional> opt = ArgumentSupport.getInstancesOfSubClass(arguments, Optional.class);
        Assert.assertTrue(opt.isEmpty());

        LinkedList<String> regex = ArgumentSupport.mapToRegexList(arguments, Required.class);
        System.out.println(Arrays.toString(regex.toArray()));
    }
}
