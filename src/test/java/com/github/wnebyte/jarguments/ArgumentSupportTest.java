package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Collection;
import org.junit.Test;
import org.junit.Assert;
import com.github.wnebyte.jarguments.adapter.TypeAdapterRegistry;
import com.github.wnebyte.jarguments.util.*;
import static com.github.wnebyte.jarguments.ArgumentSupport.getArguments;

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

        Collection<Optional> opt = getArguments(set, Optional.class);
        Assert.assertEquals(3, opt.size());
        Collection<Flag> flag = getArguments(set, Flag.class);
        Assert.assertEquals(2, flag.size());
    }
}
