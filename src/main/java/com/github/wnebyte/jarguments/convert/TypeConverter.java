package com.github.wnebyte.jarguments.convert;

import com.github.wnebyte.jarguments.exception.ParseException;

/**
 * This interface declares methods for converting a <code>String</code> into an arbitrary <code>Type</code>.
 * @param <T> the target <code>Type</code>.
 */
public interface TypeConverter<T> {

    /**
     * Converts the specified <code>String</code> into a new instance of the target <code>Type</code>.
     * @param value the value to be converted.
     * @return a new instance of the target <code>Type</code>.
     * @throws ParseException if unsuccessful.
     */
    T convert(String value) throws ParseException;

    /**
     * @return an appropriate default value for the target <code>Type</code>.
     */
    T defaultValue();
}