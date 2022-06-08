package com.github.wnebyte.jarguments.conf;

import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.exception.*;
import com.github.wnebyte.jarguments.formatter.Formatter;
import com.github.wnebyte.jarguments.parser.Parser;
import com.github.wnebyte.jarguments.util.TokenSequence;
import com.github.wnebyte.jarguments.util.ArgumentFactory;

public class ConfigurationTest {

    private final Configuration conf = new Configuration();

    private final Parser parser = new Parser();

    @Test
    public void test00() {
        String input = "KNN ~/data/data.csv";
        Set<Argument> arguments = getArguments();
        boolean success = parse(input, arguments);
        Assert.assertTrue(success);
    }

    @Test
    public void test01() {
        String input = "ABS ~/data/data.csv";
        Set<Argument> arguments = getArguments();
        parse(input, arguments);
    }

    public boolean parse(String input, Set<Argument> arguments) {
        if (input == null || arguments == null) {
            throw new NullPointerException(
                    "Input and/or Arguments must not be null."
            );
        }
        try {
            boolean success = parser.parse(input, TokenSequence.tokenize(input), arguments);
            return success;
        }
        catch (NoSuchArgumentException e) {
            Formatter<NoSuchArgumentException> formatter
                    = conf.getFormatter(NoSuchArgumentException.class);
            conf.err().println(formatter.apply(e));
        }
        catch (MissingArgumentException e) {
            Formatter<MissingArgumentException> formatter
                    = conf.getFormatter(MissingArgumentException.class);
            conf.err().println(formatter.apply(e));
        }
        catch (MalformedArgumentException e) {
            Formatter<MalformedArgumentException> formatter
                    = conf.getFormatter(MalformedArgumentException.class);
            conf.err().println(formatter.apply(e));
        }
        catch (TypeConversionException e) {
            Formatter<TypeConversionException> formatter
                    = conf.getFormatter(TypeConversionException.class);
            conf.err().println(formatter.apply(e));
        }
        catch (ConstraintException e) {
            Formatter<ConstraintException> formatter
                    = conf.getFormatter(ConstraintException.class);
            conf.err().println(formatter.apply(e));
        }
        catch (Exception ignored) { }

        return false;
    }

    private Set<Argument> getArguments() {
        ArgumentFactory factory = new ArgumentFactory();
        // required arguments
        factory.create(null, null, true,
                new String[]{ "DT", "KNN", "NB", "NN", "SVM" }, "MODEL", null, String.class);
        factory.create(null, null, true,
                null, "DATA_PATH", null, String.class);
        // optional arguments
        factory.create("-v, --verbose", null, false,
                null, null, null, boolean.class);
        return factory.getAll();
    }
}
