package com.github.wnebyte.jarguments.val;

import java.util.List;

public abstract class AbstractValidator<T> {

    protected final T source;

    public AbstractValidator(T source) {
        this.source = source;
    }

    public abstract boolean validate(String input);

    protected abstract List<String> split(String input);
}
