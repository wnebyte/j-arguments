package com.github.wnebyte.jarguments;

import java.util.regex.Pattern;

public abstract class AbstractPatternGenerator<T> {

    protected T source;

    public AbstractPatternGenerator(T source) {
        this.source = source;
    }

    public AbstractPatternGenerator() {}

    public abstract String generateRegex();

    public abstract Pattern generatePattern();

    protected T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}