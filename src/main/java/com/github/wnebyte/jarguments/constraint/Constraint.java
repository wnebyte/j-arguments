package com.github.wnebyte.jarguments.constraint;

public interface Constraint<T> {

    boolean holds(final T t);

    String errorMessage();
}
