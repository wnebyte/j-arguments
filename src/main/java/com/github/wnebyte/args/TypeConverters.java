package com.github.wnebyte.args;

import com.github.wnebyte.args.exception.ParseException;
import com.github.wnebyte.args.util.Objects;
import com.github.wnebyte.args.util.Strings;
import java.lang.reflect.Array;
import java.util.*;

public class TypeConverters implements AbstractTypeConverters {

    private static final TypeConverters instance = new TypeConverters();

    private final Map<Class<?>, TypeConverter<?>> TYPE_CONVERTERS = new HashMap<>();

    private final Map<Class<?>, TypeConverter<?>> LIST_TYPE_CONVERTERS = new HashMap<>();

    public TypeConverters() {
        put(byte.class, BYTE_TYPE_CONVERTER);
        put(byte[].class, BYTE_ARRAY_TYPE_CONVERTER);
        put(Byte.class, BYTE_TYPE_CONVERTER);
        put(Byte[].class, arrayTypeConverterOf(Byte.class));
        put(boolean.class, BOOLEAN_TYPE_CONVERTER);
        put(boolean[].class, BOOLEAN_ARRAY_TYPE_CONVERTER);
        put(Boolean.class, BOOLEAN_TYPE_CONVERTER);
        put(Boolean[].class, arrayTypeConverterOf(Boolean.class));
        put(char.class, CHARACTER_TYPE_CONVERTER);
        put(char[].class, CHAR_ARRAY_TYPE_CONVERTER);
        put(Character.class, CHARACTER_TYPE_CONVERTER);
        put(Character[].class, arrayTypeConverterOf(Character.class));
        put(double.class, DOUBLE_TYPE_CONVERTER);
        put(double[].class, DOUBLE_ARRAY_TYPE_CONVERTER);
        put(Double.class, DOUBLE_TYPE_CONVERTER);
        put(Double[].class, arrayTypeConverterOf(Double.class));
        put(float.class, FLOAT_TYPE_CONVERTER);
        put(float[].class, FLOAT_ARRAY_TYPE_CONVERTER);
        put(Float.class, FLOAT_TYPE_CONVERTER);
        put(Float[].class, arrayTypeConverterOf(Float.class));
        put(int.class, INTEGER_TYPE_CONVERTER);
        put(int[].class, INT_ARRAY_TYPE_CONVERTER);
        put(Integer.class, INTEGER_TYPE_CONVERTER);
        put(Integer[].class, arrayTypeConverterOf(Integer.class));
        put(long.class, LONG_TYPE_CONVERTER);
        put(long[].class, LONG_ARRAY_TYPE_CONVERTER);
        put(Long.class, LONG_TYPE_CONVERTER);
        put(Long[].class, arrayTypeConverterOf(Long.class));
        put(short.class, SHORT_TYPE_CONVERTER);
        put(short[].class, SHORT_ARRAY_TYPE_CONVERTER);
        put(Short.class, SHORT_TYPE_CONVERTER);
        put(Short[].class, arrayTypeConverterOf(Short.class));
        put(String.class, STRING_TYPE_CONVERTER);
        put(String[].class, arrayTypeConverterOf(String.class));
    }

    public static TypeConverters getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> TypeConverter<T[]> arrayTypeConverterOf(
            final Class<T> componentType, final TypeConverter<T> typeConverter
    ) {
        if ((componentType == null) || (typeConverter == null)) { return null; }

        return new TypeConverter<T[]>() {
            @Override
            public T[] convert(final String value) throws ParseException {
                try {
                    List<String> elements = Strings.splitByComma(value);
                    T[] array = (T[]) Array.newInstance(componentType, elements.size());
                    int i = 0;
                    for (String element : elements) {
                        String val = Splitter.normalize(element);
                        array[i++] = typeConverter.convert(val);
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

    public <T> TypeConverter<List<T>> listTypeConverterOf(
            final Class<T> componentType, final TypeConverter<T> typeConverter
    ) {
        if ((componentType == null) || (typeConverter == null)) { return null; }

        return new TypeConverter<List<T>>() {
            @Override
            public List<T> convert(final String value) throws ParseException {
                TypeConverter<T[]> arrayTypeConverter = arrayTypeConverterOf(componentType, typeConverter);
                return Arrays.asList(arrayTypeConverter.convert(value));
            }
            @Override
            public List<T> defaultValue() {
                return null;
            }
        };
    }

    @Override
    public <T> void put(final Class<T> cls, final TypeConverter<T> typeConverter) {
        if ((cls == null) || (typeConverter == null)) {
            return;
        }
        TYPE_CONVERTERS.put(cls, typeConverter);
    }

    /*
    private <T> void putList(final Class<T> cls, final TypeConverter<List<T>> typeConverter) {
        if ((cls == null) || (typeConverter == null)) {
            return;
        }
        LIST_TYPE_CONVERTERS.put(cls, typeConverter);
    }

    @SuppressWarnings("unchecked")
    public <T> TypeConverter<List<T>> getList(final Class<T> cls) {
        return (TypeConverter<List<T>>) LIST_TYPE_CONVERTERS.get(cls);
    }

    @SuppressWarnings("unchecked")
    public <T> TypeConverter<Collection<T>> getCollection(final Class<T> cls) {
        return (TypeConverter<Collection<T>>) LIST_TYPE_CONVERTERS.get(cls);
    }
     */

    @Override
    public <T> boolean putIfAbsent(final Class<T> cls, final TypeConverter<T> typeConverter) {
        if ((cls == null) || (typeConverter == null)) {
            return false;
        }
        return TYPE_CONVERTERS.putIfAbsent(cls, typeConverter) == null;
    }

    @Override
    public <T> boolean exists(final Class<T> cls) {
        return TYPE_CONVERTERS.containsKey(cls);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeConverter<T> get(final Class<T> cls) {
        return (TypeConverter<T>)TYPE_CONVERTERS.get(cls);
    }

    public final <T> TypeConverter<T[]> arrayTypeConverterOf(final Class<T> componentType) {
        return arrayTypeConverterOf(componentType, get(componentType));
    }

    public final <T> TypeConverter<List<T>> listTypeConverterOf(final Class<T> componentType) {
        return listTypeConverterOf(componentType, get(componentType));
    }

    public final TypeConverter<Boolean> BOOLEAN_TYPE_CONVERTER = new TypeConverter<Boolean>() {
        @Override
        public Boolean convert(final String value) throws ParseException {
            return Boolean.parseBoolean(value);
        }
        @Override
        public Boolean defaultValue() {
            return false;
        }
    };

    public final TypeConverter<boolean[]> BOOLEAN_ARRAY_TYPE_CONVERTER = new TypeConverter<boolean[]>() {
        @Override
        public boolean[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                boolean[] array = new boolean[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Splitter.normalize(element);
                    array[i++] = BOOLEAN_TYPE_CONVERTER.convert(val);
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

    public final TypeConverter<Byte> BYTE_TYPE_CONVERTER = new TypeConverter<Byte>() {
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

    public final TypeConverter<byte[]> BYTE_ARRAY_TYPE_CONVERTER = new TypeConverter<byte[]>() {
        @Override
        public byte[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                byte[] array = new byte[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Splitter.normalize(element);
                    array[i++] = BYTE_TYPE_CONVERTER.convert(val);
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
            return new byte[0];
        }
    };

    public final TypeConverter<Character> CHARACTER_TYPE_CONVERTER = new TypeConverter<Character>() {
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

    public final TypeConverter<char[]> CHAR_ARRAY_TYPE_CONVERTER = new TypeConverter<char[]>() {
        @Override
        public char[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                char[] array = new char[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Splitter.normalize(element);
                    array[i++] = CHARACTER_TYPE_CONVERTER.convert(val);
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

    public final TypeConverter<Double> DOUBLE_TYPE_CONVERTER = new TypeConverter<Double>() {
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

    public final TypeConverter<double[]> DOUBLE_ARRAY_TYPE_CONVERTER = new TypeConverter<double[]>() {
        @Override
        public double[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                double[] array = new double[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Splitter.normalize(element);
                    array[i++] = DOUBLE_TYPE_CONVERTER.convert(val);
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

    public final TypeConverter<Float> FLOAT_TYPE_CONVERTER = new TypeConverter<Float>() {
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

    public final TypeConverter<float[]> FLOAT_ARRAY_TYPE_CONVERTER = new TypeConverter<float[]>() {
        @Override
        public float[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                float[] array = new float[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Splitter.normalize(element);
                    array[i++] = FLOAT_TYPE_CONVERTER.convert(val);
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

    public final TypeConverter<Integer> INTEGER_TYPE_CONVERTER = new TypeConverter<Integer>() {
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

    public final TypeConverter<int[]> INT_ARRAY_TYPE_CONVERTER = new TypeConverter<int[]>() {
        @Override
        public int[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                int[] array = new int[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Splitter.normalize(element);
                    array[i++] = INTEGER_TYPE_CONVERTER.convert(val);
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

    public final TypeConverter<Long> LONG_TYPE_CONVERTER = new TypeConverter<Long>() {
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

    public final TypeConverter<long[]> LONG_ARRAY_TYPE_CONVERTER = new TypeConverter<long[]>() {
        @Override
        public long[] convert(final String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                long[] array = new long[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Splitter.normalize(element);
                    array[i++] = LONG_TYPE_CONVERTER.convert(val);
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

    public final TypeConverter<Short> SHORT_TYPE_CONVERTER = new TypeConverter<Short>() {
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

    public final TypeConverter<short[]> SHORT_ARRAY_TYPE_CONVERTER = new TypeConverter<short[]>() {
        @Override
        public short[] convert(String value) throws ParseException {
            try {
                List<String> elements = Strings.splitByComma(value);
                short[] array = new short[elements.size()];
                int i = 0;
                for (String element : elements) {
                    String val = Splitter.normalize(element);
                    array[i++] = SHORT_TYPE_CONVERTER.convert(val);
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

    public final TypeConverter<String> STRING_TYPE_CONVERTER = new TypeConverter<String>() {
        @Override
        public String convert(final String value) throws ParseException {
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
        if (!(o instanceof TypeConverters)) { return false; }
        TypeConverters typeConverters = (TypeConverters) o;
        return Objects.equals(typeConverters.TYPE_CONVERTERS, this.TYPE_CONVERTERS) &&
                super.equals(typeConverters);
    }

    @Override
    public int hashCode() {
        int result = 55;
        return result +
                Objects.hashCode(this.TYPE_CONVERTERS) +
                super.hashCode();
    }

    @Override
    public String toString() {
        return String.format("[TypeConverters.class typeConverters: %s]", TYPE_CONVERTERS);
    }
}