package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.exception.ConstraintException;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactoryBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentIParserTest {

    @Test
    public void test00() throws ParseException, ConstraintException {
        List<Argument> arguments = new ArgumentCollectionFactoryBuilder().build()
                .setNames("-a", "--a", "---a")
                .setType(String.class)
                .setIsOptional()
                .setDefaultValue("def0")
                .append()
                .setNames("-b", "--b", "---b")
                .setIsOptional()
                .setDefaultValue("def1")
                .setType(String.class)
                .append()
                .get();
        Object[] args = new ArgumentCollectionParser(arguments).parse("");
    }

    @Test
    public void test01() {
        ArgumentCollectionParser parser = new ArgumentCollectionParser(new ArrayList<>());
        List<String> list = parser.split("");
        System.out.println("size: " + list.size() + Arrays.toString(list.toArray()));
    }
}
