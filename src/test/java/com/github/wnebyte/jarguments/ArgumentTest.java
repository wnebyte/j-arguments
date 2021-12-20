package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.constraint.Constraint;
import com.github.wnebyte.jarguments.constraint.ConstraintCollectionBuilder;
import com.github.wnebyte.jarguments.convert.TypeConverterMap;
import com.github.wnebyte.jarguments.exception.ConstraintException;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.factory.ArgumentFactoryBuilder;
import com.github.wnebyte.jarguments.pattern.DeprecatedCollectionPatternGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class ArgumentTest {

    @Test
    public void test00() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a", "--a")
                .setIsRequired()
                .setType(String.class)
                .append()
                .setName("-b", "--b")
                .setIsRequired()
                .setType(String.class)
                .append()
                .get();
        Pattern pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(matches(pattern,
                "--a hello --b 'hello there'",
                "-b \"hello there\" --a 'hey there'"
        ));
        Assert.assertFalse(matches(pattern,
                "---a hello there"
        ));
        String input = "--a \"a: 'val' b: 'val2'\"";
    }

    @Test(expected = ConstraintException.class)
    public void test01() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a", "--a")
                .setIsRequired()
                .append(Integer.class, new ConstraintCollectionBuilder<Integer>()
                        .addConstraint(new Constraint<Integer>() {
                            @Override
                            public boolean holds(Integer i) {
                                return i == 10;
                            }
                            @Override
                            public String errorMessage() {
                                return null;
                            }
                        })
                        .build())
                .get();
        Argument arg = arguments.get(0);
        Integer i = (Integer) arg.initialize("9");
    }

    @Test
    public void testToString() {
        List<Argument> args = new ArgumentFactoryBuilder().build()
                .setIsPositional()
                .append(int.class)
                .setName("-a", "-A")
                .setIsRequired()
                .append(int.class)
                .setName("-b", "-B")
                .setIsOptional()
                .setDefaultValue("5")
                .append(int.class)
                .setName("-c", "-C")
                .setType(boolean.class)
                .append()
                .get();
        Argument pos = args.get(0);
        Argument req = args.get(1);
        Argument opt = args.get(2);
        Argument c = args.get(3);
        System.out.println(pos);
        System.out.println(req);
        System.out.println(opt);
        System.out.println("");
        System.out.println(pos.toPaddedDescriptiveString());
        System.out.println(req.toPaddedDescriptiveString());
        System.out.println(opt.toDescriptiveString());
        System.out.println(c.toDescriptiveString());
    }

    @Test
    public void test02() {
        Flag flag = new Flag(
                new HashSet<>(),
                "",
                0,
                boolean.class,
                TypeConverterMap.getInstance().BOOLEAN_TYPE_CONVERTER,
                "true",
                "false"
        );
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
