package com.github.wnebyte.jarguments.adapter;

import com.github.wnebyte.jarguments.exception.ParseException;

/**
 * This interface declares methods for converting a <code>String</code> into an arbitrary type.
 * @param <T> the target type for the Conversion.
 */
public interface TypeAdapter<T> {

    /**
     * Converts the specified <code>value</code> into a new instance of type <code>T</code>.
     * @param value a String value to be converted.
     * @return a new instance of type T.
     * @throws ParseException if unsuccessful.
     */
    T convert(String value) throws ParseException;

    /**
     * Return an appropriate default value of type <code>T</code>.
     * @return a new default value of type T.
     */
    T defaultValue();
}