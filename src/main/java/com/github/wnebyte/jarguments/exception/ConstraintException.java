package com.github.wnebyte.jarguments.exception;

public class ConstraintException extends ParseException {

    public ConstraintException(String message) {
        super(message);
    }

    public ConstraintException(Throwable cause) {
        super(cause);
    }
}
