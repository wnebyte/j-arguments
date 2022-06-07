package com.github.wnebyte.jarguments.adapter;

import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.Assert;

public class TypeAdapterRegistryTest {

    @Test
    public void testGet00() {
        AbstractTypeAdapterRegistry converters = TypeAdapterRegistry.getInstance();
        TypeAdapter<Integer> intConverter = converters.get(int.class);
        Assert.assertNotNull(intConverter);
        TypeAdapter<UUID> uuidConverter = converters.get(UUID.class);
        Assert.assertNull(uuidConverter);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testGet01() {
        AbstractTypeAdapterRegistry typeConverters = TypeAdapterRegistry.getInstance();
        TypeAdapter<String> stringConverter = typeConverters.get(String.class);
        Assert.assertNotNull(stringConverter);
        TypeAdapter<String[]> stringArrayConverter = typeConverters.get(String[].class);
        Assert.assertNotNull(stringArrayConverter);
        TypeAdapter<List> listTypeAdapter = typeConverters.get(List.class);
        Assert.assertNull(listTypeAdapter);
        TypeAdapter<Integer> integerTypeAdapter = typeConverters.get(null);
        Assert.assertNull(integerTypeAdapter);
    }
}
