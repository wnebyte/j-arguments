package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class MissingArgumentException extends ParseException {

    private final String input;

    private final Argument missing;

    public MissingArgumentException(String message, String input, Argument missing) {
        super(message);
        this.input = input;
        this.missing = missing;
    }

    public Argument getMissingArgument() {
        return missing;
    }

    public String getInput() {
        return input;
    }
}
