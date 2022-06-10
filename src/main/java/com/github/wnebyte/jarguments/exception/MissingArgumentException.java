package com.github.wnebyte.jarguments.exception;

import com.github.wnebyte.jarguments.Argument;

public class MissingArgumentException extends ParseException {

    public MissingArgumentException(String message) {
        super(message);
    }

    public MissingArgumentException(
            String message,
            Argument argument,
            String token,
            String input
    ) {
        super(message, argument, token, input);
    }

    public MissingArgumentException(Throwable cause) {
        super(cause);
    }

    public MissingArgumentException(
            Throwable cause,
            Argument argument,
            String token,
            String input)
    {
        super(cause, argument, token, input);
    }
}
