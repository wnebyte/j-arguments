package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class TypeConversionException extends ParseException {

    private final Argument argument;

    private final String input;

    private final String token;

    public TypeConversionException(String msg, Argument argument, String token, String input) {
        super(msg);
        this.argument = argument;
        this.token = token;
        this.input = input;
    }

    public Argument getArgument() {
        return argument;
    }

    public String getInput() {
        return input;
    }

    public String getToken() {
        return token;
    }
}
