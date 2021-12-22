package com.github.wnebyte.jarguments.exception;

public class NoSuchArgumentException extends ParseException {

    private final String input;

    private final String token;

    public NoSuchArgumentException(String message, String input, String token) {
        super(message);
        this.input = input;
        this.token = token;
    }

    public String getInput() {
        return input;
    }

    public String getToken() {
        return token;
    }
}
