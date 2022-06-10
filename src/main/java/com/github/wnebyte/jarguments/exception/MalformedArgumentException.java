package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class MalformedArgumentException extends ParseException {

    public MalformedArgumentException(String message) {
        super(message);
    }

    public MalformedArgumentException(
            String message,
            Argument argument,
            String token,
            String input
    ) {
        super(message, argument, token, input);
    }

    public MalformedArgumentException(Throwable cause) {
        super(cause);
    }

    public MalformedArgumentException(
            Throwable cause,
            Argument argument,
            String token,
            String input
    ) {
        super(cause, argument, token, input);
    }
}
