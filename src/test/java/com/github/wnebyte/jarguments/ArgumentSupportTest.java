package com.github.wnebyte.jarguments;

import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.Assert;
import com.github.wnebyte.jarguments.factory.ArgumentFactory;
import com.github.wnebyte.jarguments.factory.ArgumentFactoryBuilder;
import static com.github.wnebyte.jarguments.ArgumentSupport.getArguments;

public class ArgumentSupportTest {

    @Test
    public void testGetArguments00() {
        ArgumentFactory factory = new ArgumentFactoryBuilder().build();
        // 1 positional
        // 2 required
        List<Argument> c = factory
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

        Assert.assertEquals(3, c.size());
        Collection<Positional> positionalList = ArgumentSupport.getArguments(c, Positional.class);
        Assert.assertEquals(1, positionalList.size());
        Collection<Required> requiredList = ArgumentSupport.getArguments(c, Required.class);
        Assert.assertEquals(3, requiredList.size());
        Collection<Optional> optionalList = ArgumentSupport.getArguments(c, Optional.class);
        Assert.assertTrue(optionalList.isEmpty());
    }

    @Test
    public void testGetArguments01() {
        // 2 optional
        // 1 flag
        List<Argument> c = new ArgumentFactory()
                .setName("-a")
                .isOptional()
                .append(int.class)
                .setName("-b")
                .isFlag()
                .append(boolean.class)
                .setName("-c")
                .isFlag()
                .append(boolean.class)
                .get();

        Collection<Optional> c1 = getArguments(c, Optional.class);
        Assert.assertEquals(3, c1.size());
        Collection<Flag> c2 = getArguments(c, Flag.class);
        Assert.assertEquals(2, c2.size());
    }
}
