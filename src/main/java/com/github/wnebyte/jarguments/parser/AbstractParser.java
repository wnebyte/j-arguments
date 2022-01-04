package com.github.wnebyte.jarguments.parser;

import com.github.wnebyte.jarguments.exception.ParseException;

public abstract class AbstractParser<T extends Iterable<String>, R> {

    public abstract Object[] initialize() throws ParseException;

    public abstract void parse(T t, R r) throws ParseException;

    public abstract void reset();
}
