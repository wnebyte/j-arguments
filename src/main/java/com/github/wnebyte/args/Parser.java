package com.github.wnebyte.args;

import com.github.wnebyte.args.exception.ConstraintException;
import com.github.wnebyte.args.exception.ParseException;
import java.util.Collection;

public interface Parser {

    Object[] parse(final String input) throws ParseException, ConstraintException;

    Collection<String> split(final String input);
}