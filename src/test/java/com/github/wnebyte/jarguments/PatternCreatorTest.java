package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactory;
import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactoryBuilder;
import org.junit.Test;

import java.util.List;

public class PatternCreatorTest {

    @Test
    public void test00() {
        ArgumentCollectionFactory argumentCollectionFactory = new ArgumentCollectionFactoryBuilder().build();
        List<Argument> arguments = argumentCollectionFactory
                .setNames("-h", "--h")
                .isPositional()
                .append(int.class)
                .setNames("-a", "--a")
                .isRequired()
                .append(int.class)
                .setNames("-b", "--b")
                .isRequired()
                .append(int.class)
                .get();

    }
}
