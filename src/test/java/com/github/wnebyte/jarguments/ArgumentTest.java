package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.converter.TypeConverter;
import com.github.wnebyte.jarguments.factory.ArgumentCollectionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ArgumentTest {

    @Test
    public void test00() {
        List<Argument> arguments = new ArgumentCollectionFactoryBuilder().build()
                .setNames("-a", "--a")
                .isRequired()
                .setType(String.class)
                .append()
                .setNames("-b", "--b")
                .isRequired()
                .setType(String.class)
                .append()
                .get();
        Pattern pattern = new ArgumentCollectionPatternGenerator(arguments).generatePattern();
        Assert.assertTrue(matches(pattern,
                "--a hello --b 'hello there'",
                "-b \"hello there\" --a 'hey there'"
        ));
        Assert.assertFalse(matches(pattern,
                "---a hello there"
        ));
        String input = "--a \"a: 'val' b: 'val2'\"";
    }

    @Test
    public void testToString() {
        List<Argument> args = new ArgumentCollectionFactoryBuilder().build()
                .isPositional()
                .append(int.class)
                .setNames("-a", "-A")
                .isRequired()
                .append(int.class)
                .setNames("-b", "-B")
                .isOptional()
                .append(int.class)
                .get();
        Argument pos = args.get(0);
        Argument req = args.get(1);
        Argument opt = args.get(2);
        System.out.println(pos);
        System.out.println(req);
        System.out.println(opt);
    }

    private <T extends Collection<R>, R> void cons(
            Class<? super T> type,
            Class<R> componentType,
            TypeConverter<T> typeConverter
    ) {
        Required req = new Required(
                Collections.singleton("*"),
                "desc",
                0,
                type,
                typeConverter
        );
    }

    private void cons(
            Class<?> type,
            TypeConverter<?> typeConverter
    ) {

    }

    private boolean matches(final Pattern pattern, final String... input) {
        for (String s : input) {
            boolean matches = pattern.matcher(s).matches();
            if (!matches) {
                return false;
            }
        }
        return true;
    }
}
