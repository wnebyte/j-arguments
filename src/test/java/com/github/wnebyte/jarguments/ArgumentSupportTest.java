package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactory;
import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentSupportTest {

    @Test
    public void test00() {
        ArgumentCollectionFactory argumentCollectionFactory = new ArgumentCollectionFactoryBuilder().build();
        List<Argument> arguments = argumentCollectionFactory
                .setNames("-h", "--h")
                .setIsPositional()
                .append(int.class)
                .setNames("-a", "--a")
                .setIsRequired()
                .append(int.class)
                .setNames("-b", "--b")
                .setIsRequired()
                .append(int.class)
                .get();

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
