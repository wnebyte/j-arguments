package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactoryBuilder;
import org.junit.Test;
import java.util.List;

public class ArgumentCollectionFactoryTest {

    @Test
    public void test00() {
        List<Argument> arguments = new ArgumentCollectionFactoryBuilder().build()
                .setNames("-a", "--a")
                .setDescription("test arg")
                .isRequired()
                .setType(float.class)
                .append()
                .get();
    }

    @Test
    public void test01() {
        List<Argument> arguments = new ArgumentCollectionFactoryBuilder().build()
                .setNames("-a")
                .setType(String.class)
                .isOptional()
                .setDefaultValue("def 0")
                .append()
                .get();
    }
}