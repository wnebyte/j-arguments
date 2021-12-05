package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.exception.ParseException;
import java.util.Collection;

public interface IParser {

    Object[] parse(final String input) throws ParseException;

    Collection<String> split(final String input);
}