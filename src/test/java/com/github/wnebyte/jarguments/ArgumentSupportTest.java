package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Collection;
import org.junit.Test;
import org.junit.Assert;
import com.github.wnebyte.jarguments.util.*;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.adapter.TypeAdapterRegistry;

public class ArgumentSupportTest {

    private final TypeAdapterRegistry adapters
            = TypeAdapterRegistry.getInstance();

    @Test
    public void getArgumentsTest00() {
        Set<Argument> set = Sets.newSet();
        set.add(new PositionalBuilder<Integer>()
                .setIndex(0)
                .setPosition(0)
                .setType(Integer.class)
                .setTypeAdapter(adapters.INTEGER_TYPE_ADAPTER)
                .build());
        set.add(new RequiredBuilder<Integer>()
                .setIndex(1)
                .setNames(Sets.of("-a", "--a"))
                .setType(Integer.class)
                .setTypeAdapter(adapters.INTEGER_TYPE_ADAPTER)
                .build());
        set.add(new RequiredBuilder<Integer>()
                .setIndex(2)
                .setNames(Sets.of("-b", "--b"))
                .setType(Integer.class)
                .setTypeAdapter(adapters.INTEGER_TYPE_ADAPTER)
                .build());
        Assert.assertEquals(3, set.size());
        Collection<Positional> pos = ArgumentSupport.getArguments(set, Positional.class);
        Assert.assertEquals(1, pos.size());
        Collection<Required> req = ArgumentSupport.getArguments(set, Required.class);
        Assert.assertEquals(3, req.size());
        Collection<Optional> opt = ArgumentSupport.getArguments(set, Optional.class);
        Assert.assertTrue(opt.isEmpty());
    }

    @Test
    public void getArgumentsTest01() {
        Set<Argument> set = Sets.newSet();
        set.add(new OptionalBuilder<Integer>()
                .setIndex(0)
                .setNames(Sets.of("-a"))
                .setType(Integer.class)
                .setTypeAdapter(adapters.INTEGER_TYPE_ADAPTER)
                .build());
        set.add(new FlagBuilder<Boolean>()
                .setIndex(1)
                .setNames(Sets.of("-b"))
                .setType(Boolean.class)
                .setTypeAdapter(adapters.BOOLEAN_TYPE_ADAPTER)
                .setValue("true")
                .setDefaultValue("false")
                .build());
        set.add(new FlagBuilder<Boolean>()
                .setIndex(2)
                .setNames(Sets.of("-c"))
                .setType(Boolean.class)
                .setTypeAdapter(adapters.BOOLEAN_TYPE_ADAPTER)
                .setValue("true")
                .setDefaultValue("false")
                .build());
        Collection<Optional> opt = ArgumentSupport.getArguments(set, Optional.class);
        Assert.assertEquals(3, opt.size());
        Collection<Flag> flag = ArgumentSupport.getArguments(set, Flag.class);
        Assert.assertEquals(2, flag.size());
    }

    @Test
    public void containsAnyTest00() {
        Set<Argument> set = Sets.newSet();
        set.add(new OptionalBuilder<Integer>()
                .setIndex(0)
                .setNames(Sets.of("-a"))
                .setType(Integer.class)
                .setTypeAdapter(adapters.INTEGER_TYPE_ADAPTER)
                .build());
        set.add(new FlagBuilder<Boolean>()
                .setIndex(1)
                .setNames(Sets.of("-b"))
                .setType(Boolean.class)
                .setTypeAdapter(adapters.BOOLEAN_TYPE_ADAPTER)
                .setValue("true")
                .setDefaultValue("false")
                .build());
        set.add(new FlagBuilder<Boolean>()
                .setIndex(2)
                .setNames(Sets.of("-c"))
                .setType(Boolean.class)
                .setTypeAdapter(adapters.BOOLEAN_TYPE_ADAPTER)
                .setValue("true")
                .setDefaultValue("false")
                .build());
        boolean contains = ArgumentSupport.containsAny(set, Optional.class);
        Assert.assertTrue(contains);
        contains = ArgumentSupport.containsAny(set, Flag.class);
        Assert.assertTrue(contains);
        contains = ArgumentSupport.containsAny(set, Required.class);
        Assert.assertFalse(contains);
    }

    @Test
    public void getByPositionTest00() {
        Set<Argument> set = Sets.newSet();
        set.add(new PositionalBuilder<Integer>()
                .setIndex(0)
                .setPosition(0)
                .setType(Integer.class)
                .setTypeAdapter(adapters.INTEGER_TYPE_ADAPTER)
                .build());
        set.add(new RequiredBuilder<Integer>()
                .setIndex(1)
                .setNames(Sets.of("-a", "--a"))
                .setType(Integer.class)
                .setTypeAdapter(adapters.INTEGER_TYPE_ADAPTER)
                .build());
        set.add(new RequiredBuilder<Integer>()
                .setIndex(2)
                .setNames(Sets.of("-b", "--b"))
                .setType(Integer.class)
                .setTypeAdapter(adapters.INTEGER_TYPE_ADAPTER)
                .build());
        set.add(new PositionalBuilder<String>()
                .setIndex(3)
                .setPosition(1)
                .setType(String.class)
                .setTypeAdapter(adapters.STRING_TYPE_ADAPTER)
                .build());
        Argument pos = ArgumentSupport.getByPosition(set, 0);
        Assert.assertNotNull(pos);
        Assert.assertEquals(Integer.class, pos.getType());
        pos = ArgumentSupport.getByPosition(set, 1);
        Assert.assertNotNull(pos);
        Assert.assertEquals(String.class, pos.getType());
        pos = ArgumentSupport.getByPosition(set, 2);
        Assert.assertNull(pos);
        pos = ArgumentSupport.getByPosition(null, 0);
        Assert.assertNull(pos);
        pos = ArgumentSupport.getByPosition(set, -1);
        Assert.assertNull(pos);
    }

    @Test
    public void initializeTest00() throws ParseException {
        Argument argument = new PositionalBuilder<Integer>()
                .setIndex(0)
                .setPosition(0)
                .setType(Integer.class)
                .setTypeAdapter(adapters.INTEGER_TYPE_ADAPTER)
                .build();
        String value = "200";
        int val = (int)ArgumentSupport.initialize(argument, value);
        Assert.assertEquals(200, val);
    }
}
