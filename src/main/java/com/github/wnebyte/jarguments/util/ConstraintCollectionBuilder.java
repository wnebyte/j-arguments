package com.github.wnebyte.jarguments.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import com.github.wnebyte.jarguments.Constraint;

public class ConstraintCollectionBuilder<T> {

    private final List<Constraint<T>> constraints;

    public ConstraintCollectionBuilder() {
        this.constraints = new ArrayList<>();
    }

    public ConstraintCollectionBuilder<T> addConstraint(Constraint<T> constraint) {
        if (constraint != null) {
            constraints.add(constraint);
        }
        return this;
    }

    public Collection<Constraint<T>> build() {
        return constraints;
    }
}