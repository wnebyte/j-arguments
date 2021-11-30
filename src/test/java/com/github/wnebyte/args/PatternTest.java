package com.github.wnebyte.args;

import com.github.wnebyte.args.factory.ArgumentFactoryBuilder;
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
        Pattern pattern = new PatternCreator().create(
                new ArgumentFactoryBuilder().build()
                        .setNames("-b")
                        .isPositional()
                        .create(int.class)
                        .setNames("-c")
                        .isPositional()
                        .create(int.class)
                        .setNames("-a")
                        .isOptional()
                        .create(boolean.class)
                        .getArguments()
        );
    }

    @Test
    public void test05() {
        Pattern pattern = new PatternCreator().create(
                new ArgumentFactoryBuilder().build()
                        .isPositional()
                        .create(String.class)
                        .getArguments()
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
