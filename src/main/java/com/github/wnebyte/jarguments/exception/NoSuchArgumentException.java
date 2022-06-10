package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class NoSuchArgumentException extends ParseException {

    public NoSuchArgumentException(String message) {
        super(message);
    }

    public NoSuchArgumentException(
            String message,
            Argument argument,
            String token,
            String input
    ) {
        super(message, argument, token, input);
    }

    public NoSuchArgumentException(Throwable cause) {
        super(cause);
    }

    public NoSuchArgumentException(
            Throwable cause,
            Argument argument,
            String token,
            String input
    ) {
        super(cause, argument, token, input);
    }
}
