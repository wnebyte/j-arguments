package com.github.wnebyte.jarguments;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.Assert;
import com.github.wnebyte.jarguments.factory.ArgumentFactory;
import com.github.wnebyte.jarguments.factory.ArgumentFactoryBuilder;

public class ArgumentSupportTest {

    @Test
    public void test00() {
        ArgumentFactory argumentCollectionFactory = new ArgumentFactoryBuilder().build();
        List<Argument> arguments = argumentCollectionFactory
                .setName("-h", "--h")
                .isPositional()
                .append(int.class)
                .setName("-a", "--a")
                .isRequired()
                .append(int.class)
                .setName("-b", "--b")
                .isRequired()
                .append(int.class)
                .get();

        List<Positional> positionalList = ArgumentSupport.getArguments(arguments, Positional.class);
        Assert.assertEquals(1, positionalList.size());
        List<Required> requiredList = ArgumentSupport.getArguments(arguments, Required.class);
        Assert.assertEquals(2, requiredList.size());
        List<Optional> optionalList = ArgumentSupport.getArguments(arguments, Optional.class);
        Assert.assertTrue(optionalList.isEmpty());
        List<String> regex = ArgumentSupport.regexList(arguments, Required.class);
        System.out.println(Arrays.toString(regex.toArray()));
    }
}
