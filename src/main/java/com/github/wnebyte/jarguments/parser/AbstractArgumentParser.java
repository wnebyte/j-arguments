package com.github.wnebyte.jarguments.parser;

import java.util.LinkedList;
import com.github.wnebyte.jarguments.exception.ParseException;

public abstract class AbstractArgumentParser<T> {

    protected final T source;

    public AbstractArgumentParser(T source) {
        this.source = source;
    }

    public abstract Object[] parse(String input) throws ParseException;

    protected abstract LinkedList<String> split(String input);
}