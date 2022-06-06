package com.github.wnebyte.jarguments;

public interface Constraint<T> {

    boolean test(T t);

    String errorMessage();
}
