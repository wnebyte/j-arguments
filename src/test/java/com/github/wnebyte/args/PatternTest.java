package com.github.wnebyte.args;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
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
        Pattern pattern = new PatternCreator().createPattern(
                new ArgumentFactoryBuilder().build()
                        .setNames("-b")
                        .setPositional()
                        .create(int.class)
                        .setNames("-c")
                        .setPositional()
                        .create(int.class)
                        .setNames("-a")
                        .setOptional()
                        .create(boolean.class)
                        .getArguments(),
                true
        );
    }

    @Test
    public void test05() {
        Pattern pattern = new PatternCreator().createPattern(
                new ArgumentFactoryBuilder().build()
                        .setPositional()
                        .create(String.class)
                        .getArguments(),
                true
        );
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
