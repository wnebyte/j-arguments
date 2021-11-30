package com.github.wnebyte.args;

import java.util.List;
import java.util.regex.Pattern;

public interface IPatternCreator {
    
    Pattern create(final List<Argument> args);
}
