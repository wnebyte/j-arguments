package com.github.wnebyte.jarguments;

public interface Constraint<T> {

    boolean verify(T t);

    String errorMessage();
}
