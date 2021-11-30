package com.github.wnebyte.args.constraint;

public interface Constraint<T> {

    boolean holds(final T t);

    String errorMessage();
}
