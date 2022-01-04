package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.factory.ArgumentFactory;
import com.github.wnebyte.jarguments.factory.ArgumentFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentSupportTest {

    @Test
    public void test00() {
        ArgumentFactory argumentCollectionFactory = new ArgumentFactoryBuilder().build();
        List<Argument> arguments = argumentCollectionFactory
                .setName("-h", "--h")
                .setIsPositional()
                .append(int.class)
                .setName("-a", "--a")
                .setIsRequired()
                .append(int.class)
                .setName("-b", "--b")
                .setIsRequired()
                .append(int.class)
                .get();

        List<Positional> pos = ArgumentSupport.getInstancesOfSubClass(arguments, Positional.class);
        Assert.assertEquals(1, pos.size());
        List<Required> req = ArgumentSupport.getInstancesOfSubClass(arguments, Required.class);
        Assert.assertEquals(2, req.size());
        List<Optional> opt = ArgumentSupport.getInstancesOfSubClass(arguments, Optional.class);
        Assert.assertTrue(opt.isEmpty());

        LinkedList<String> regex = ArgumentSupport.mapToRegex(arguments, Required.class);
        System.out.println(Arrays.toString(regex.toArray()));
    }
}
