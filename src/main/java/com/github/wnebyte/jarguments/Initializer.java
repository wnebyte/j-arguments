package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.exception.ParseException;

@FunctionalInterface
public interface Initializer<T> {

    T apply(String value) throws ParseException;
}