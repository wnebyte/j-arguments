package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class MissingArgumentException extends ParseException {

    private final String input;

    private final Argument argument;

    public MissingArgumentException(String message, String input, Argument argument) {
        super(message);
        this.input = input;
        this.argument = argument;
    }

    public MissingArgumentException(Throwable cause, String input, Argument argument) {
        super(cause);
        this.input = input;
        this.argument = argument;
    }

    public Argument getArgument() {
        return argument;
    }

    public String getInput() {
        return input;
    }
}
