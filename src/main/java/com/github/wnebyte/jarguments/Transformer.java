package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.exception.ParseException;

public interface Transformer<T> {

    T transform(final String value) throws ParseException;
}