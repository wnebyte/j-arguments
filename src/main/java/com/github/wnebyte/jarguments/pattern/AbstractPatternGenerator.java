package com.github.wnebyte.jarguments.pattern;

import java.util.regex.Pattern;

public abstract class AbstractPatternGenerator<T> {

    protected T source;

    public AbstractPatternGenerator(T source) {
        this.source = source;
    }

    public abstract String getRegex();

    public abstract Pattern getPattern();

    protected final T getSource() {
        return source;
    }

    public final void setSource(T source) {
        this.source = source;
    }
}