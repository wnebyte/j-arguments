package com.github.wnebyte.jarguments.convert;

import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.Assert;

public class TypeConverterMapTest {

    @Test
    public void testGet00() {
        AbstractTypeConverterMap converters = TypeConverterMap.getInstance();
        TypeConverter<Integer> intConverter = converters.get(int.class);
        Assert.assertNotNull(intConverter);
        TypeConverter<UUID> uuidConverter = converters.get(UUID.class);
        Assert.assertNull(uuidConverter);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testGet01() {
        AbstractTypeConverterMap typeConverters = TypeConverterMap.getInstance();
        TypeConverter<String> stringConverter = typeConverters.get(String.class);
        Assert.assertNotNull(stringConverter);
        TypeConverter<String[]> stringArrayConverter = typeConverters.get(String[].class);
        Assert.assertNotNull(stringArrayConverter);
        TypeConverter<List> listTypeConverter = typeConverters.get(List.class);
        Assert.assertNull(listTypeConverter);
        TypeConverter<Integer> integerTypeConverter = typeConverters.get(null);
        Assert.assertNull(integerTypeConverter);
    }
}
