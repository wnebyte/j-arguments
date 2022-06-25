package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class ParseException extends Exception {

    private Argument argument;

    private String input;

    private String token;

    public ParseException(String message) {
        this(message, null, null, null);
    }

    public ParseException(
            String message,
            Argument argument,
            String token,
            String input
    ) {
        super(message);
        this.argument = argument;
        this.token = token;
        this.input = input;
    }

    public ParseException(Throwable cause) {
        this(cause, null, null, null);
    }

    public ParseException(
            Throwable cause,
            Argument argument,
            String token,
            String input
    ) {
        super(cause);
        this.argument = argument;
        this.token = token;
        this.input = input;
    }

    public Argument getArgument() {
        return argument;
    }

    public void initArgument(Argument argument) {
        this.argument = argument;
    }

    public String getInput() {
        return input;
    }

    public void initInput(String input) {
        this.input = input;
    }

    public String getToken() {
        return token;
    }

    public void initToken(String token) {
        this.token = token;
    }
}