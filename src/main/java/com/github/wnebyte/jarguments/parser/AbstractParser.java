package com.github.wnebyte.jarguments.parser;

import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.exception.ParseException;

public interface AbstractParser {

    Object[] initialize() throws ParseException;

    boolean parse(String input, Iterable<String> tokens, Iterable<Argument> arguments) throws ParseException;

    void clear();
}
