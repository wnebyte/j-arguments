package com.github.wnebyte.jarguments.adapter;

import org.junit.Test;
import java.util.Arrays;

public class TypeAdapterTest {

    @Test
    public void integerArrayConversionTest00() throws Exception {
        TypeAdapterRegistry typeConverters = TypeAdapterRegistry.getInstance();
        TypeAdapter<Integer[]> typeAdapter = typeConverters.arrayTypeAdapterOf(Integer.class);
        Integer[] arr = typeAdapter.convert("1,2,3,4,5");
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void intArrayConversionTest00() throws Exception {
        TypeAdapter<int[]> typeAdapter = TypeAdapterRegistry.getInstance().INT_ARRAY_TYPE_ADAPTER;
        int[] arr = typeAdapter.convert("1,2,3,4,5");
        System.out.println(Arrays.toString(arr));
    }
}