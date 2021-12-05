package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.converter.TypeConverter;
import com.github.wnebyte.jarguments.converter.TypeConverterMap;
import org.junit.Test;
import java.util.Arrays;

public class TypeConvertersTest {

    @Test
    public void test01() throws Exception {
        TypeConverterMap typeConverters = TypeConverterMap.getInstance();
        TypeConverter<Integer[]> typeConverter = typeConverters.arrayTypeConverterOf(Integer.class);
        Integer[] arr = typeConverter.convert("1,2,3,4,5");
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void test02() throws Exception {
        TypeConverter<int[]> typeConverter = TypeConverterMap.getInstance().INT_ARRAY_TYPE_CONVERTER;
        int[] arr = typeConverter.convert("1,2,3,4,5");
        System.out.println(Arrays.toString(arr));
    }
}