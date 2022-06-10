package com.github.wnebyte.jarguments.adapter;

import com.github.wnebyte.jarguments.exception.TypeConversionException;

/**
 * This interface declares a method for converting a <code>String</code> into another type.
 * @param <T> the target type of the Conversion.
 */
public interface TypeAdapter<T> {

    /**
     * Converts the specified <code>value</code> into a new instance of type <code>T</code>.
     * @param value a value.
     * @return a new instance.
     * @throws TypeConversionException if unsuccessful.
     */
    T convert(String value) throws TypeConversionException;

    /**
     * Return an appropriate default value.
     * @return a new default value of type T.
     */
    T defaultValue();
}