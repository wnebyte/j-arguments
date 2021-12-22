package com.github.wnebyte.jarguments.val;

import java.util.List;
import com.github.wnebyte.jarguments.exception.ParseException;

public abstract class AbstractValidator<T> {

    protected final T source;

    public AbstractValidator(T source) {
        this.source = source;
    }

    public abstract boolean validate(String input);

    public abstract boolean matches(String input) throws ParseException;

    protected abstract List<String> split(String input);

    protected final T getSource() {
        return source;
    }
}
