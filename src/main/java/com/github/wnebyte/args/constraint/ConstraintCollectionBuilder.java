package com.github.wnebyte.args.constraint;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ConstraintCollectionBuilder<T> {

    private final List<Constraint<T>> constraints;

    public ConstraintCollectionBuilder() {
        this.constraints = new LinkedList<>();
    }

    public ConstraintCollectionBuilder<T> addConstraint(Constraint<T> constraint) {
        constraints.add(constraint);
        return this;
    }

    public Collection<Constraint<T>> build() {
        return constraints;
    }
}