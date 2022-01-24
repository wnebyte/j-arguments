package com.github.wnebyte.jarguments.exception;

public class ParseException extends Exception {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }
}