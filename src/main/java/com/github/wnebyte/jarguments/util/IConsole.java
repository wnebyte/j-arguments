package com.github.wnebyte.jarguments.util;

import java.io.InputStream;
import java.io.PrintStream;

public interface IConsole {

    PrintStream out();

    PrintStream err();

    InputStream in();
}
