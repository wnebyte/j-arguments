package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class TypeConversionException extends ParseException {

    private final Argument argument;

    private final String input;

    private final String value;

    public TypeConversionException(String msg, Argument argument, String input, String value) {
        super(msg);
        this.argument = argument;
        this.input = input;
        this.value = value;
    }

    public Argument getArgument() {
        return argument;
    }

    public String getInput() {
        return input;
    }

    public String getValue() {
        return value;
    }
}
