package com.github.wnebyte.jarguments;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import com.github.wnebyte.jarguments.adapter.TypeAdapterRegistry;
import org.junit.Test;
import com.github.wnebyte.jarguments.exception.ConstraintException;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.factory.ArgumentFactory;
import com.github.wnebyte.jarguments.factory.ArgumentFactoryBuilder;
import com.github.wnebyte.jarguments.util.ConstraintCollectionBuilder;

public class ArgumentTest {

    @Test(expected = ConstraintException.class)
    public void test01() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a", "--a")
                .isRequired()
                .append(Integer.class, new ConstraintCollectionBuilder<Integer>()
                        .addConstraint(new Constraint<Integer>() {
                            @Override
                            public boolean verify(Integer i) {
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
                .isPositional()
                .append(int.class)
                .setName("-a", "-A")
                .isRequired()
                .append(int.class)
                .setName("-b", "-B")
                .isOptional()
                .setDefaultValue("5")
                .append(int.class)
                .setName("-c", "-C")
                .isFlag()
                .setType(boolean.class)
                .append()
                .get();

        for (Argument arg : args) {
            System.out.println(arg.toString());
            System.out.println(arg.toPaddedString());
            System.out.println(arg.toDescriptiveString());
            System.out.println(arg.toPaddedDescriptiveString() + "\n");
        }
    }

    /*
    @Test
    public void test02() {
        Flag flag = new Flag(
                new HashSet<>(),
                "",
                0,
                boolean.class,
                TypeAdapterRegistry.getInstance().BOOLEAN_TYPE_CONVERTER,
                "true",
                "false"
        );
    }
     */

    @Test
    public void test03() {
        List<Argument> args = ArgumentFactory.builder().build()
                .setName("-a", "--a")
                .isRequired()
                .append(int.class)
                .isPositional()
                .append(int.class)
                .setName("--flag")
                .isFlag()
                .append(boolean.class)
                .isPositional()
                .append(int.class)
                .setName("-b", "--b")
                .isOptional()
                .append(int.class)
                .get();
        args.sort(Argument::compareTo);
        System.out.println(Arrays.toString(args.toArray()));
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
