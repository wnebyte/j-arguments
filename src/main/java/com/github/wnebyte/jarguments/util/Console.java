package com.github.wnebyte.jarguments.util;

import java.io.InputStream;
import java.io.PrintStream;

public class Console implements IConsole {

    @Override
    public PrintStream out() {
        return System.out;
    }

    @Override
    public PrintStream err() {
        return System.err;
    }

    @Override
    public InputStream in() {
        return System.in;
    }
}
