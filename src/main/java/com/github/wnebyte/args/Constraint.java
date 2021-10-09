package com.github.wnebyte.args;

public interface Constraint<T> {

    boolean holds(final T t);

    String errorMessage();
}
