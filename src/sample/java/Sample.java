import java.util.Set;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.BaseConfiguration;
import com.github.wnebyte.jarguments.ContextView;
import com.github.wnebyte.jarguments.exception.*;
import com.github.wnebyte.jarguments.Formatter;
import com.github.wnebyte.jarguments.parser.AbstractParser;
import com.github.wnebyte.jarguments.parser.Parser;
import com.github.wnebyte.jarguments.util.ArgumentFactory;
import com.github.wnebyte.jarguments.util.Objects;
import com.github.wnebyte.jarguments.util.TokenSequence;

public class Sample {

    public static void main(String[] args) {
        Sample sample = new Sample(null);
        String input = "PATH DB";
        Set<Argument> arguments = getArguments();
        sample.parse(input, arguments);
    }

    private final BaseConfiguration conf;

    private final AbstractParser parser;

    public Sample(BaseConfiguration conf) {
        this.conf = Objects.requireNonNullElseGet(conf, BaseConfiguration::new);
        this.parser = new Parser();
    }

    public Object[] parse(String input, Set<Argument> arguments) {
        if (input == null || arguments == null) {
            throw new NullPointerException(
                    "Input and/or Arguments must not be null."
            );
        }
        try {
            Object[] vals = parser.parse(input, TokenSequence.tokenize(input), arguments);
            return vals;
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

        Formatter<ContextView> formatter = conf.getHelpFormatter();
        conf.out().println(formatter.apply(ContextView.of(arguments)));

        return null;
    }

    private static Set<Argument> getArguments() {
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
