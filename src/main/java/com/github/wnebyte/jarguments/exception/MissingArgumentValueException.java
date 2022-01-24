package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class MissingArgumentValueException extends ParseException {

    private final String input;

    private final Argument argument;

    public MissingArgumentValueException(String message, String input, Argument argument) {
        super(message);
        this.argument = argument;
        this.input = input;
    }

    public MissingArgumentValueException(Throwable cause, String input, Argument argument) {
        super(cause);
        this.argument = argument;
        this.input = input;
    }

    public Argument getArgument() {
        return argument;
    }

    public String getInput() {
        return input;
    }
}
