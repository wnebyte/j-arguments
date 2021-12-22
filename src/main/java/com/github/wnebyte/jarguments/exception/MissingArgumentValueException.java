package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class MissingArgumentValueException extends ParseException {

    private final Argument missing;

    private final String input;

    public MissingArgumentValueException(String message, String input, Argument missing) {
        super(message);
        this.missing = missing;
        this.input = input;
    }

    public Argument getMissingArgument() {
        return missing;
    }

    public String getInput() {
        return input;
    }
}
