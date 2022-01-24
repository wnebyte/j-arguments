package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class MalformedArgumentException extends ParseException {

    private final String input;

    private final Argument arg;

    public MalformedArgumentException(String message, String input, Argument arg) {
        super(message);
        this.input = input;
        this.arg = arg;
    }

    public MalformedArgumentException(Throwable cause, String input, Argument arg) {
        super(cause);
        this.input = input;
        this.arg = arg;
    }

    public String getInput() {
        return input;
    }

    public Argument getArgument() {
        return arg;
    }
}
