package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

public class PatternTest {

    @Test
    public void test00() {
        String regex = "[^\\s\"']*|\"[^\"']*\"|'[^'\"]*'|\\$\\(.*\\)";
        Pattern pattern = Pattern.compile(regex);
        Assert.assertTrue(matches(pattern,
                "heeello",
                "'hello govna'",
                "\"hello govna\"",
                "$(add -a 5 -b 10)"
                ));
    }

    @Test
    public void test01() {
        String regex = "([^\\s\"']*|\"[^\"']*\"|'[^'\"]*'|\\$\\(.*\\))";
        String map = "\\{" + regex + "*\\}";
        Pattern pattern = Pattern.compile(map);
        System.out.println(matches(pattern,
                "{a:'test',b:'hello var'}"
        ));
    }

    @Test
    public void test03() {
        Pattern pattern = new ArgumentCollectionPatternGenerator(
                new ArgumentCollectionFactoryBuilder().build()
                        .setNames("-b")
                        .setIsPositional()
                        .append(int.class)
                        .setNames("-c")
                        .setIsPositional()
                        .append(int.class)
                        .setNames("-a")
                        .setIsOptional()
                        .append(boolean.class)
                        .get()
        ).generatePattern();
    }

    @Test
    public void test05() {
        Pattern pattern = new ArgumentCollectionPatternGenerator(
                new ArgumentCollectionFactoryBuilder().build()
                        .setIsPositional()
                        .append(String.class)
                        .get())
                .generatePattern();
        Assert.assertTrue(matches(pattern,
                "hej",
                "'hej'",
                "'hello there'",
                "\"hello there\"",
                "\"hello 'hello there'\"",
                "'hello \"hello there\"'"
        ));
    }

    private boolean matches(final Pattern pattern, final String input) {
        return pattern.matcher(input).matches();
    }

    private boolean matches(final Pattern pattern, final String... input) {
        for (String s : input) {
            boolean matches = matches(pattern, s);
            if (!matches) {
                return false;
            }
        }
        return true;
    }
}
