package com.github.wnebyte.jarguments.util;

import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import com.github.wnebyte.jarguments.*;
import com.github.wnebyte.jarguments.adapter.TypeAdapterRegistry;

public class ArgumentFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void unknownAdapterTest00() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create(null, null, true,
                null, "UUID", null, UUID.class);
        factory.create("bar", null, true,
                null, null, null, String.class);
        factory.create("b", null, true,
                null, null, null, int.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalCharacterTest00() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("$", null, true,
                null, null, null, int.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalNameTest00() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("--help", null, true,
                null, null, null, int.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicateNameTest00() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("--name", null, true,
                null, null, null, int.class);
        factory.create("--test", null, true,
                null, null, null, int.class);
        factory.create("--name", null, true,
                null, null, null, int.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicateNameTest01() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("a, --name", null, true,
                null, null, null, int.class);
        factory.create("test", null, true,
                null, null, null, int.class);
        factory.create("--a, --name", null, true,
                null, null, null, int.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void typeMismatchTest00() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("-a", null, false,
                null, null, "hello", int.class,
                TypeAdapterRegistry.getInstance().INTEGER_TYPE_ADAPTER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void typeMismatchTest01() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("-a", null, false,
                null, null, "test", int.class,
                TypeAdapterRegistry.getInstance().INTEGER_TYPE_ADAPTER, null);
    }

    @Test
    public void subClassConstructionTest00() {
        ArgumentFactory factory = new ArgumentFactory();
        Argument a = factory.create("-a", null, false,
                null, null, null, boolean.class);
        Argument b = factory.create(null, null, true,
                null, null, null, int.class);
        Argument c = factory.create("-c", null, false,
                null, null, null, String.class);
        Argument d = factory.create("-d", null, true,
                null, null, null, int.class);
        Assert.assertSame(a.getClass(), Flag.class);
        Assert.assertSame(b.getClass(), Positional.class);
        Assert.assertSame(c.getClass(), Optional.class);
        Assert.assertSame(d.getClass(), Required.class);
    }
}
