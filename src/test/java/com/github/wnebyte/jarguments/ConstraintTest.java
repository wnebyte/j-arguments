package com.github.wnebyte.jarguments;

import java.util.Collection;
import org.junit.Test;
import com.github.wnebyte.jarguments.util.ConstraintCollectionBuilder;

public class ConstraintTest {

    @Test
    public void test00() {
        Collection<Constraint<Integer>> constraints = new ConstraintCollectionBuilder<Integer>()
                .addConstraint(new Constraint<Integer>() {
                    @Override
                    public boolean verify(Integer integer) {
                        return false;
                    }
                    @Override
                    public String errorMessage() {
                        return null;
                    }
                })
                .addConstraint(new Constraint<Integer>() {
                    @Override
                    public boolean verify(Integer integer) {
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
