package com.github.wnebyte.jarguments;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.io.PrintStream;
import com.github.wnebyte.jarguments.exception.*;
import com.github.wnebyte.jarguments.util.IConsole;
import com.github.wnebyte.jarguments.formatter.Formatter;
import com.github.wnebyte.jarguments.formatter.HelpFormatter;

/**
 * This class represents a configuration.
 */
public class Configuration {

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

    protected PrintStream out
            = System.out;

    protected PrintStream err
            = System.err;

    protected Formatter<Set<Argument>> helpFormatter
            = DEFAULT_HELP_FORMATTER;

    private final Map<Class<? extends ParseException>, Formatter<? extends ParseException>> formatters
            = new HashMap<>();

    /*
    ###########################
    #      CONSTRUCTORS       #
    ###########################
    */

    /**
     * Constructs a new instance.
     */
    public Configuration() {
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

    @SuppressWarnings("unchecked")
    public <T extends ParseException> Formatter<T> getFormatter(Class<T> cls) {
        if (cls != null) {
            if (formatters.containsKey(cls)) {
                Formatter<?> formatter = formatters.get(cls);
                return (Formatter<T>) formatter;
            }
        }
        return null;
    }

    public boolean hasFormatter(Class<?> cls) {
        return formatters.containsKey(cls);
    }

    public <T extends ParseException> Configuration setFormatter(Class<T> cls, Formatter<T> formatter) {
        if (cls != null && formatter != null) {
            formatters.put(cls, formatter);
        }
        return this;
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
