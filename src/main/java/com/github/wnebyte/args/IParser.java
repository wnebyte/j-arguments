package com.github.wnebyte.args;

import com.github.wnebyte.args.exception.ParseException;
import java.util.Collection;

public interface IParser {

    Object[] parse(final String input) throws ParseException;

    Collection<String> split(final String input);
}