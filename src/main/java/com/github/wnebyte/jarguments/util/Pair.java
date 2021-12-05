package com.github.wnebyte.jarguments.util;

public class Pair<T, R> {

    private T first;

    private R last;

    public Pair() { }

    public Pair(final T first, final R last) {
        this.first = first;
        this.last = last;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(final T first) {
        this.first = first;
    }

    public R getLast() {
        return last;
    }

    public void setLast(final R last) {
        this.last = last;
    }
}