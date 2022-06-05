package com.github.wnebyte.jarguments.adapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TestRegistry {

    private Map<Type, TypeAdapter<?>> adapters = new HashMap<>();

    private static class Type {

        Class<?> type;

        Class<?>[] paramTypes;

        Type(Class<?> type, Class<?>[] paramTypes) {
            this.type = type;
            this.paramTypes = paramTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (!(o instanceof Type)) return false;
            Type type = (Type) o;
            return this.type.equals(type.type) &&
                    Arrays.equals(this.paramTypes, type.paramTypes);
        }
    }
}
