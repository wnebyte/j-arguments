package com.github.wnebyte.jarguments;

import java.util.LinkedList;
import com.github.wnebyte.jarguments.exception.ParseException;

public abstract class AbstractParser<T> {

    protected T source;

    public AbstractParser(T source) {
        this.source = source;
    }

    public AbstractParser() { }

    public abstract Object[] parse(String input) throws ParseException;

    protected abstract LinkedList<String> split(String input);

    protected T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}