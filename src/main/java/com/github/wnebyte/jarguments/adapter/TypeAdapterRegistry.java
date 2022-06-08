package com.github.wnebyte.jarguments.adapter;

import java.util.*;
import java.lang.reflect.Array;
import com.github.wnebyte.jarguments.util.Normalizer;
import com.github.wnebyte.jarguments.util.Objects;
import com.github.wnebyte.jarguments.util.Strings;
import com.github.wnebyte.jarguments.exception.ParseException;

public class TypeAdapterRegistry extends AbstractTypeAdapterRegistry {

    /*
    ###########################
    #      STATIC METHODS     #
    ###########################
    */

    public static TypeAdapterRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TypeAdapterRegistry();
        }
        return INSTANCE;
    }

    public static <T> TypeAdapter<T[]> arrayTypeAdapterOf(
            final Class<T> componentType, final TypeAdapter<T> typeAdapter
    ) {
        if (componentType == null || typeAdapter == null) {
            return null;
        }
        return new TypeAdapter<T[]>() {
            @Override
            public T[] convert(final String value) throws ParseException {
                try {
                    List<String> elements = Strings.splitByComma(value);
                    @SuppressWarnings("unchecked")
                    T[] array = (T[]) Array.newInstance(componentType, elements.size());
                    int i = 0;
                    for (String element : elements) {
                        String val = Normalizer.normalize(element);
                        array[i++] = typeAdapter.convert(val);
                    }
                    return array;
                } catch (Exception e) {
                    throw new ParseException(
                            e.getMessage()
                    );
                }
            }
            @Override
            public T[] defaultValue() {
                return null;
            }
        };
    }

    public static <T> TypeAdapter<List<T>> listTypeAdapterOf(
            final Class<T> componentType, final TypeAdapter<T> typeAdapter
    ) {
        if (componentType == null || typeAdapter == null) {
            return null;
        }
        TypeAdapter<T[]> arrayTypeAdapter = arrayTypeAdapterOf(componentType, typeAdapter);

        return new TypeAdapter<List<T>>() {
            @Override
            public List<T> convert(String value) throws ParseException {
                T[] array = arrayTypeAdapter.convert(value);
                return Arrays.asList(array);
            }
            @Override
            public List<T> defaultValue() {
                return null;
            }
        };
    }

    public static <T> TypeAdapter<Collection<T>> collectionTypeAdapterOf(
            final Class<T> componentType, final TypeAdapter<T> typeAdapter
    ) {
        if (componentType == null || typeAdapter == null) {
            return null;
        }
        TypeAdapter<List<T>> listTypeAdapter = listTypeAdapterOf(componentType, typeAdapter);

        return new TypeAdapter<Collection<T>>() {
            @Override
            public Collection<T> convert(String value) throws ParseException {
                return listTypeAdapter.convert(value);
            }
            @Override
            public Collection<T> defaultValue() {
                return null;
            }
        };
    }

    /*
    ###########################
    #       STATIC FIELDS     #
    ###########################
    */

    private static TypeAdapterRegistry INSTANCE = null;

    /*
    ###########################
    #       CONSTRUCTORS      #
    ###########################
    */

    public TypeAdapterRegistry() {
        super.adapters = new HashMap<>(9 * 4);
        init();
    }

    /*
    ###########################
    #         METHODS         #
    ###########################
    */

    private void init() {
        register(byte.class, BYTE_TYPE_ADAPTER);
        register(byte[].class, BYTE_ARRAY_TYPE_ADAPTER);
        register(Byte.class, BYTE_TYPE_ADAPTER);
        register(Byte[].class, arrayTypeAdapterOf(Byte.class));
        register(boolean.class, BOOLEAN_TYPE_ADAPTER);
        register(boolean[].class, BOOLEAN_ARRAY_TYPE_ADAPTER);
        register(Boolean.class, BOOLEAN_TYPE_ADAPTER);
        register(Boolean[].class, arrayTypeAdapterOf(Boolean.class));
        register(char.class, CHARACTER_TYPE_ADAPTER);
        register(char[].class, CHAR_ARRAY_TYPE_ADAPTER);
        register(Character.class, CHARACTER_TYPE_ADAPTER);
        register(Character[].class, arrayTypeAdapterOf(Character.class));
        register(double.class, DOUBLE_TYPE_ADAPTER);
        register(double[].class, DOUBLE_ARRAY_TYPE_ADAPTER);
        register(Double.class, DOUBLE_TYPE_ADAPTER);
        register(Double[].class, arrayTypeAdapterOf(Double.class));
        register(float.class, FLOAT_TYPE_ADAPTER);
        register(float[].class, FLOAT_ARRAY_TYPE_ADAPTER);
        register(Float.class, FLOAT_TYPE_ADAPTER);
        register(Float[].class, arrayTypeAdapterOf(Float.class));
        register(int.class, INTEGER_TYPE_ADAPTER);
        register(int[].class, INT_ARRAY_TYPE_ADAPTER);
        register(Integer.class, INTEGER_TYPE_ADAPTER);
        register(Integer[].class, arrayTypeAdapterOf(Integer.class));
        register(long.class, LONG_TYPE_ADAPTER);
        register(long[].class, LONG_ARRAY_TYPE_ADAPTER);
        register(Long.class, LONG_TYPE_ADAPTER);
        register(Long[].class, arrayTypeAdapterOf(Long.class));
        register(short.class, SHORT_TYPE_ADAPTER);
        register(short[].class, SHORT_ARRAY_TYPE_ADAPTER);
        register(Short.class, SHORT_TYPE_ADAPTER);
        register(Short[].class, arrayTypeAdapterOf(Short.class));
        register(String.class, STRING_TYPE_ADAPTER);
        register(String[].class, arrayTypeAdapterOf(String.class));
    }

    public final <T> TypeAdapter<T[]> arrayTypeAdapterOf(final Class<T> componentType) {
        return arrayTypeAdapterOf(componentType, get(componentType));
    }

    public final <T> TypeAdapter<List<T>> listTypeAdapterOf(final Class<T> componentType) {
        return listTypeAdapterOf(componentType, get(componentType));
    }

    @Override
    public <T> void register(final Class<T> type, final TypeAdapter<T> typeAdapter) {
        if (type == null || typeAdapter == null) {
            throw new IllegalArgumentException(
                    "Type & TypeAdapter must not be null."
            );
        }
        adapters.put(type, typeAdapter);
    }

    @Override
    public <T> boolean registerIfAbsent(final Class<T> type, final TypeAdapter<T> typeAdapter) {
        if (type == null || typeAdapter == null) {
            return false;
        }
        return (adapters.putIfAbsent(type, typeAdapter) == null);
    }

    @Override
    public boolean isRegistered(final Class<?> type) {
        if (type == null) {
            return false;
        } else {
            return adapters.containsKey(type);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> get(final Class<T> type) {
        if (type == null) {
            return null;
        }
        TypeAdapter<?> typeAdapter = adapters.get(type);
        if (typeAdapter != null) {
            return (TypeAdapter<T>) typeAdapter;
        } else {
            return null;
        }
    }

    public <T> TypeAdapter<? extends Collection<T>> get(
            final Class<? extends Collection> type,
            final Class<T> componentType
    ) {
        if (type == List.class) {
            return listTypeAdapterOf(componentType);
        }

        return null;
    }

    /*
    ###########################
    #         ADAPTERS        #
    ###########################
    */

    public final TypeAdapter<Boolean> BOOLEAN_TYPE_ADAPTER = new TypeAdapter<Boolean>() {
        @Override
        public Boolean convert(final String value) throws ParseException {
            try {
                return Boolean.parseBoolean(value);
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public Boolean defaultValue() {
            return false;
        }
    };

    public final TypeAdapter<boolean[]> BOOLEAN_ARRAY_TYPE_ADAPTER = new TypeAdapter<boolean[]>() {
        @Override
        public boolean[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                boolean[] array = new boolean[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Normalizer.normalize(element);
                    array[i++] = BOOLEAN_TYPE_ADAPTER.convert(val);
                }
                return array;
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public boolean[] defaultValue() {
            return null;
        }
    };

    public final TypeAdapter<Byte> BYTE_TYPE_ADAPTER = new TypeAdapter<Byte>() {
        @Override
        public Byte convert(final String value) throws ParseException {
            try {
                return Byte.parseByte(value);
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public Byte defaultValue() {
            return (byte) 0;
        }
    };

    public final TypeAdapter<byte[]> BYTE_ARRAY_TYPE_ADAPTER = new TypeAdapter<byte[]>() {
        @Override
        public byte[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                byte[] array = new byte[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Normalizer.normalize(element);
                    array[i++] = BYTE_TYPE_ADAPTER.convert(val);
                }
                return array;
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public byte[] defaultValue() {
            return null;
        }
    };

    public final TypeAdapter<Character> CHARACTER_TYPE_ADAPTER = new TypeAdapter<Character>() {
        @Override
        public Character convert(final String value) throws ParseException {
            try {
                return value.charAt(0);
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public Character defaultValue() {
            return '\u0000';
        }
    };

    public final TypeAdapter<char[]> CHAR_ARRAY_TYPE_ADAPTER = new TypeAdapter<char[]>() {
        @Override
        public char[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                char[] array = new char[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Normalizer.normalize(element);
                    array[i++] = CHARACTER_TYPE_ADAPTER.convert(val);
                }
                return array;
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public char[] defaultValue() {
            return null;
        }
    };

    public final TypeAdapter<Double> DOUBLE_TYPE_ADAPTER = new TypeAdapter<Double>() {
        @Override
        public Double convert(final String value) throws ParseException {
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public Double defaultValue() {
            return 0.0d;
        }
    };

    public final TypeAdapter<double[]> DOUBLE_ARRAY_TYPE_ADAPTER = new TypeAdapter<double[]>() {
        @Override
        public double[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                double[] array = new double[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Normalizer.normalize(element);
                    array[i++] = DOUBLE_TYPE_ADAPTER.convert(val);
                }
                return array;
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public double[] defaultValue() {
            return null;
        }
    };

    public final TypeAdapter<Float> FLOAT_TYPE_ADAPTER = new TypeAdapter<Float>() {
        @Override
        public Float convert(final String value) throws ParseException {
            try {
                return Float.parseFloat(value);
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public Float defaultValue() {
            return 0.0f;
        }
    };

    public final TypeAdapter<float[]> FLOAT_ARRAY_TYPE_ADAPTER = new TypeAdapter<float[]>() {
        @Override
        public float[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                float[] array = new float[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Normalizer.normalize(element);
                    array[i++] = FLOAT_TYPE_ADAPTER.convert(val);
                }
                return array;
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public float[] defaultValue() {
            return null;
        }
    };

    public final TypeAdapter<Integer> INTEGER_TYPE_ADAPTER = new TypeAdapter<Integer>() {
        @Override
        public Integer convert(final String value) throws ParseException {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public Integer defaultValue() {
            return 0;
        }
    };

    public final TypeAdapter<int[]> INT_ARRAY_TYPE_ADAPTER = new TypeAdapter<int[]>() {
        @Override
        public int[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                int[] array = new int[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Normalizer.normalize(element);
                    array[i++] = INTEGER_TYPE_ADAPTER.convert(val);
                }
                return array;
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public int[] defaultValue() {
            return null;
        }
    };

    public final TypeAdapter<Long> LONG_TYPE_ADAPTER = new TypeAdapter<Long>() {
        @Override
        public Long convert(final String value) throws ParseException {
            try {
                return Long.parseLong(value);
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public Long defaultValue() {
            return 0L;
        }
    };

    public final TypeAdapter<long[]> LONG_ARRAY_TYPE_ADAPTER = new TypeAdapter<long[]>() {
        @Override
        public long[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                long[] array = new long[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Normalizer.normalize(element);
                    array[i++] = LONG_TYPE_ADAPTER.convert(val);
                }
                return array;
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public long[] defaultValue() {
            return null;
        }
    };

    public final TypeAdapter<Short> SHORT_TYPE_ADAPTER = new TypeAdapter<Short>() {
        @Override
        public Short convert(final String value) throws ParseException {
            try {
                return Short.parseShort(value);
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public Short defaultValue() {
            return (short) 0;
        }
    };

    public final TypeAdapter<short[]> SHORT_ARRAY_TYPE_ADAPTER = new TypeAdapter<short[]>() {
        @Override
        public short[] convert(String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                short[] array = new short[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Normalizer.normalize(element);
                    array[i++] = SHORT_TYPE_ADAPTER.convert(val);
                }
                return array;
            } catch (Exception e) {
                throw new ParseException(
                        e.getMessage()
                );
            }
        }
        @Override
        public short[] defaultValue() {
            return null;
        }
    };

    public final TypeAdapter<String> STRING_TYPE_ADAPTER = new TypeAdapter<String>() {
        @Override
        public String convert(final String value) {
            return value;
        }
        @Override
        public String defaultValue() {
            return null;
        }
    };

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (o == this) { return true; }
        if (!(o instanceof TypeAdapterRegistry)) { return false; }
        TypeAdapterRegistry typeConverters = (TypeAdapterRegistry) o;
        return Objects.equals(typeConverters.adapters, this.adapters) &&
                super.equals(typeConverters);
    }

    @Override
    public int hashCode() {
        int result = 55;
        return result +
                Objects.hashCode(this.adapters) +
                super.hashCode();
    }

    @Override
    public String toString() {
        return String.format("com.github.wnebyte.jarguments.adapter.TypeAdapterRegistry(converters=%s)", adapters);
    }
}