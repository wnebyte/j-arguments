package com.github.wnebyte.jarguments.exception;

public class TypeConversionException extends ParseException {

    public TypeConversionException(String message) {
        super(message);
    }

    public TypeConversionException(Throwable cause) {
        super(cause);
    }
}
