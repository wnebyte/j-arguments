package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.constraint.ConstraintCollectionBuilder;
import org.junit.Test;

import java.util.Collection;

public class ConstraintTest {

    @Test
    public void test00() {
        Collection<Constraint<Integer>> constraints = new ConstraintCollectionBuilder<Integer>()
                .addConstraint(new Constraint<Integer>() {
                    @Override
                    public boolean holds(Integer integer) {
                        return false;
                    }

                    @Override
                    public String errorMessage() {
                        return null;
                    }
                })
                .addConstraint(new Constraint<Integer>() {
                    @Override
                    public boolean holds(Integer integer) {
                        return false;
                    }

                    @Override
                    public String errorMessage() {
                        return null;
                    }
                })
                .build();
    }
}
