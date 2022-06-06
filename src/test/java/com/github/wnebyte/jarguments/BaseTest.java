package com.github.wnebyte.jarguments;

import java.util.regex.Pattern;

public abstract class BaseTest {

    protected boolean allMatch(Pattern pattern, String... input) {
        for (String s : input) {
            boolean matches = pattern.matcher(s).matches();
            if (!matches) {
                System.err.printf(
                        "Input: '%s' does not match the given pattern.%n", s
                );
                return false;
            }
        }
        return true;
    }

    protected boolean noneMatch(Pattern pattern, String... input) {
        for (String s : input) {
            boolean matches = pattern.matcher(s).matches();
            if (matches) {
                System.err.printf(
                        "Input: '%s' does match the given pattern.%n", s
                );
                return false;
            }
        }
        return true;
    }
}