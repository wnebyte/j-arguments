package com.github.wnebyte.jarguments.convert;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class TypeConverterMapTest {

    @Test
    public void testGet() {
        AbstractTypeConverterMap converters = TypeConverterMap.getInstance();
        TypeConverter<Integer> intConverter = converters.get(int.class);
        Assert.assertNotNull(intConverter);
        TypeConverter<UUID> uuidConverter = converters.get(UUID.class);
        Assert.assertNull(uuidConverter);
    }
}
