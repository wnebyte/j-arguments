package com.github.wnebyte.args.converter;

import com.github.wnebyte.args.exception.ParseException;

/**
 * This interface declares a method for converting a String into an arbitrary type.
 * @param <T> the target type.
 */
public interface TypeConverter<T> {

    /**
     * Converts the specified <code>value</code> into a new instance of the target type.
     * @param value to be converted.
     * @return a new instance of the target type.
     * @throws ParseException if unsuccessful.
     */
    T convert(final String value) throws ParseException;

    /**
     * Returns an appropriate default value for the target type.
     * @return a default value.
     */
    T defaultValue();
}