package com.github.wnebyte.args;

import org.junit.Test;
import java.util.Arrays;

public class TypeConvertersTest {

    @Test
    public void test01() throws Exception {
        TypeConverters typeConverters = TypeConverters.getInstance();
        TypeConverter<Integer[]> typeConverter = typeConverters.arrayTypeConverterOf(Integer.class);
        Integer[] arr = typeConverter.convert("1,2,3,4,5");
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void test02() throws Exception {
        TypeConverter<int[]> typeConverter = TypeConverters.getInstance().INT_ARRAY_TYPE_CONVERTER;
        int[] arr = typeConverter.convert("1,2,3,4,5");
        System.out.println(Arrays.toString(arr));
    }
}