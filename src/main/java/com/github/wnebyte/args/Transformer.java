package com.github.wnebyte.args;

import com.github.wnebyte.args.exception.ParseException;

public interface Transformer<T> {

    T transform(final String value) throws ParseException;
}