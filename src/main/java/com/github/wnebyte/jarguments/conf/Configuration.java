package com.github.wnebyte.jarguments.conf;

import java.util.Set;
import java.io.PrintStream;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.exception.*;
import com.github.wnebyte.jarguments.util.IConsole;
import com.github.wnebyte.jarguments.formatter.Formatter;
import com.github.wnebyte.jarguments.formatter.HelpFormatter;

/**
 * This class represents a configuration.
 */
public class Configuration extends BaseConfiguration {

    /*
    ###########################
    #      STATIC METHODS     #
    ###########################
    */

    /**
     * Constructs and returns a new <code>Configuration</code> instance.
     * @return a new instance.
     */
    public static Configuration newInstance() {
        return new Configuration();
    }

    /*
    ###########################
    #      STATIC FIELDS      #
    ###########################
    */

    public static final Formatter<Set<Argument>> DEFAULT_HELP_FORMATTER
            = HelpFormatter.get();

    public static final Formatter<TypeConversionException> DEFAULT_TYPE_CONVERSION_FORMATTER
            = Throwable::getMessage;

    public static final Formatter<NoSuchArgumentException> DEFAULT_NO_SUCH_ARGUMENT_FORMATTER
            = Throwable::getMessage;

    public static final Formatter<MalformedArgumentException> DEFAULT_MALFORMED_ARGUMENT_FORMATTER
            = Throwable::getMessage;

    public static final Formatter<MissingArgumentException> DEFAULT_MISSING_ARGUMENT_FORMATTER
            = Throwable::getMessage;

    public static final Formatter<ConstraintException> DEFAULT_CONSTRAINT_FORMATTER
            = Throwable::getMessage;

    /*
    ###########################
    #          FIELDS         #
    ###########################
    */

    private PrintStream out
            = System.out;

    private PrintStream err
            = System.err;

    private Formatter<Set<Argument>> helpFormatter
            = DEFAULT_HELP_FORMATTER;

    /*
    ###########################
    #      CONSTRUCTORS       #
    ###########################
    */

    /**
     * Constructs a new instance.
     */
    public Configuration() {
        super();
        setFormatter(NoSuchArgumentException.class, DEFAULT_NO_SUCH_ARGUMENT_FORMATTER);
        setFormatter(MissingArgumentException.class, DEFAULT_MISSING_ARGUMENT_FORMATTER);
        setFormatter(MalformedArgumentException.class, DEFAULT_MALFORMED_ARGUMENT_FORMATTER);
        setFormatter(TypeConversionException.class, DEFAULT_TYPE_CONVERSION_FORMATTER);
        setFormatter(ConstraintException.class, DEFAULT_CONSTRAINT_FORMATTER);
    }

    /*
    ###########################
    #          METHODS        #
    ###########################
    */

    public PrintStream out() {
        return out;
    }

    public PrintStream err() {
        return err;
    }

    public Formatter<Set<Argument>> getHelpFormatter() {
        return helpFormatter;
    }

    public Configuration setOut(PrintStream out) {
        if (out != null) {
            this.out = out;
        }
        return this;
    }

    public Configuration setErr(PrintStream err) {
        if (err != null) {
            this.err = err;
        }
        return this;
    }

    public Configuration setConsole(IConsole console) {
        if (console != null) {
            setOut(console.out());
            setErr(console.err());
        }
        return this;
    }

    public Configuration setHelpFormatter(Formatter<Set<Argument>> formatter) {
        if (formatter != null) {
            this.helpFormatter = formatter;
        }
        return this;
    }
}
