package com.github.wnebyte.args;

import com.github.wnebyte.args.converter.TypeConverter;
import com.github.wnebyte.args.converter.TypeConverterMap;
import com.github.wnebyte.args.factory.ArgumentFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ArgumentTest {

    @Test
    public void test00() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setNames("-a", "--a")
                .isRequired()
                .setType(String.class)
                .create()
                .setNames("-b", "--b")
                .isRequired()
                .setType(String.class)
                .create()
                .getArguments();
        Pattern pattern = new PatternCreator().create(arguments);
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
        List<Argument> args = new ArgumentFactoryBuilder().build()
                .isPositional()
                .create(int.class)
                .setNames("-a", "-A")
                .isRequired()
                .create(int.class)
                .setNames("-b", "-B")
                .isOptional()
                .create(int.class)
                .getArguments();
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
