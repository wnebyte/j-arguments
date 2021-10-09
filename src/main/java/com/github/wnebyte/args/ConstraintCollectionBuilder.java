package com.github.wnebyte.args;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConstraintCollectionBuilder<T> {

    private final List<Constraint<T>> constraints = new ArrayList<>();

    public ConstraintCollectionBuilder<T> addConstraint(final Constraint<T> constraint) {
        constraints.add(constraint);
        return this;
    }

    public Collection<Constraint<T>> build() {
        return constraints;
    }
}
