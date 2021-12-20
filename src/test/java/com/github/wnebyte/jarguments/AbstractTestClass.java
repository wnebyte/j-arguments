package com.github.wnebyte.jarguments;

import java.util.Collection;
import java.util.regex.Pattern;
import com.github.wnebyte.jarguments.val.ArgumentValidator;

public abstract class AbstractTestClass {

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

    protected boolean allMatch(Collection<Argument> arguments, String... input) {
        ArgumentValidator validator = new ArgumentValidator(arguments);
        for (String s : input) {
            boolean matches = validator.validate(s);
            if (!matches) {
                System.err.printf(
                        "Input: '%s' does not match the given arguments.%n", s
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

    protected boolean noneMatch(Collection<Argument> arguments, String... input) {
        ArgumentValidator validator = new ArgumentValidator(arguments);
        for (String s : input) {
            boolean matches = validator.validate(s);
            if (matches) {
                System.err.printf(
                        "Input: '%s' does match the given arguments.%n", s
                );
                return false;
            }
        }
        return true;
    }
}