package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class ParseException extends Exception {

    private Argument argument;

    private String input;

    private String value;

    public ParseException(final String message) {
        super(message);
    }

    public void setArgument(final Argument argument) {
        this.argument = argument;
    }

    public void setInput(final String input) {
        this.input = input;
    }

    public void setValue(final String value) {
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