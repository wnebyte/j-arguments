package com.github.wnebyte.jarguments.parser;

import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.factory.ArgumentFactory;
import com.github.wnebyte.jarguments.pattern.AbstractPatternGenerator;
import com.github.wnebyte.jarguments.AbstractTestClass;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.pattern.ArgumentPatternGenerator;
import org.junit.Assert;
import org.junit.Test;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import com.github.wnebyte.jarguments.factory.ArgumentFactoryBuilder;

public class ArgumentParserTest extends AbstractTestClass {

    /*
    one REQUIRED,
    one POSITIONAL
     */
    @Test
    public void test00() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsRequired()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .get();
        AbstractPatternGenerator<Collection<Argument>> generator = new ArgumentPatternGenerator();
        generator.setSource(arguments);
        AbstractParser<Collection<Argument>> parser = new ArgumentParser(arguments);
        Pattern pattern = generator.getPattern();
        // should succeed
        String[] input = {
                "-a 5 100",
                "100 -a 5"
        };
        int[][] values = {
                {
                        5, 100
                },
                {
                        5, 100
                }
        };
        for (int i = 0; i < input.length; i++) {
            String s = input[i];
            Assert.assertTrue(allMatch(pattern, s));
            Assert.assertTrue(allMatch(arguments, s));
            Object[] args = parser.parse(s);
            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

    /*
    one OPTIONAL,
    one POSITIONAL
    */
    @Test
    public void test01() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsOptional()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .get();
        AbstractPatternGenerator<Collection<Argument>> generator = new ArgumentPatternGenerator();
        generator.setSource(arguments);
        AbstractParser<Collection<Argument>> parser = new ArgumentParser(arguments);
        Pattern pattern = generator.getPattern();
        // should succeed
        String[] input = {
                "-a 5 100",
                "100 -a 5",
                "100"
        };
        int[][] values = {
                {
                        5, 100
                },
                {
                        5, 100
                },
                {
                        0, 100
                }
        };
        for (int i = 0; i < input.length; i++) {
            String s = input[i];
            Assert.assertTrue(allMatch(pattern, s));
            Object[] args = parser.parse(s);
            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

    /*
    two OPTIONAL,
    one POSITIONAL
    */
    @Test
    public void test02() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsOptional()
                .append(int.class)
                .setName("-b")
                .setIsOptional()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .get();
        AbstractPatternGenerator<Collection<Argument>> generator = new ArgumentPatternGenerator();
        generator.setSource(arguments);
        AbstractParser<Collection<Argument>> parser = new ArgumentParser(arguments);
        Pattern pattern = generator.getPattern();
        String[] input = {
                "-a 5 -b 10 100",
                "-b 10 -a 5 100",
                "100 -a 5 -b 10",
                "100 -b 10 -a 5",
                "-a 5 100",
                "-b 10 100",
                "100 -a 5",
                "100 -b 10",
                "100"
        };
        Assert.assertTrue(allMatch(pattern, input));
        int[][] values = {
                {
                    5, 10, 100
                },
                {
                    5, 10, 100
                },
                {
                    5, 10, 100
                },
                {
                    5, 10, 100
                },
                {
                    5, 0, 100
                },
                {
                    0, 10, 100
                },
                {
                    5, 0, 100
                },
                {
                    0, 10, 100
                },
                {
                    0, 0, 100
                },
        };
        for (int i = 0; i < input.length; i++) {
            String s = input[i];
            Object[] args = parser.parse(s);
            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

    /*
    one OPTIONAL,
    one FLAG,
    two POSITIONAL
    */
    @Test
    public void test03() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsOptional()
                .append(int.class)
                .setName("-b")
                .setIsFlag()
                .append(boolean.class)
                .setIsPositional()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .get();
        AbstractPatternGenerator<Collection<Argument>> generator = new ArgumentPatternGenerator();
        generator.setSource(arguments);
        AbstractParser<Collection<Argument>> parser = new ArgumentParser(arguments);
        Pattern pattern = generator.getPattern();
        String[] input = {
                "-a 5 -b 100 50",
                "-b -a 5 100 50",
                "100 50 -a 5 -b",
                "50 100 -a 5 -b",
                "-a -b 50 100",
                "-a 50 -b 100 50",
                "50 100 -b -a 10",
                "50 100",
                "50 -a 10 100 -b",
                "50 -a 10 100",
                "100 -b 50"
        };
        Assert.assertTrue(super.allMatch(pattern, input));
        Object[][] values = {
                {
                    5, true, 100, 50
                }
        };
        Object[] args = parser.parse(input[0]);
        Assert.assertEquals(values[0][0], args[0]);
        Assert.assertEquals(values[0][1], args[1]);
        Assert.assertEquals(values[0][2], args[2]);
        Assert.assertEquals(values[0][3], args[3]);
    }

    /*
    one OPTIONAL,
    one FLAG,
    two POSITIONAL
    */
    @Test
    public void test04() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsOptional()
                .append(int.class)
                .setName("-b")
                .setIsFlag()
                .append(boolean.class)
                .setIsPositional()
                .append(int.class)
                .setIsPositional()
                .append(String.class)
                .get();
        AbstractPatternGenerator<Collection<Argument>> generator = new ArgumentPatternGenerator();
        generator.setSource(arguments);
        AbstractParser<Collection<Argument>> parser = new ArgumentParser(arguments);
        Pattern pattern = generator.getPattern();
        System.out.println(generator.getRegex().length() * 4); // ~ 5KB
        String[] input = {
                "-a 5 -b 100 'hello world'", // 1st
                "-b -a 5 100 'hello world'",
                "100 'hello world' -a 5 -b",
                "-a 5 100 -b 'hello world'",
                "-a 50 -b 100 'hello world'", // 5th
                "100 -b -a 10 'hello world'",
                "100 'hello world'",
                "100 -a 10 'hello world' -b",
                "100 -a 10 'hello world'",
                "100 -b 'hello world'" // 10th
        };
        Assert.assertTrue(allMatch(pattern, input));
        // (-a <int>) (-b) [... <int>] [... <String>]
        Object[][] values = {
                {
                    5, true, 100, "hello world" // 1st
                },
                {
                    5, true, 100, "hello world"
                },
                {
                    5, true, 100, "hello world"
                },
                {
                    5, true, 100, "hello world"
                },
                {
                    50, true, 100, "hello world" // 5 th
                },
                {
                    10, true, 100, "hello world"
                },
                {
                    0, false, 100, "hello world"
                },
                {
                    10, true, 100, "hello world"
                },
                {
                    10, false, 100, "hello world"
                },
                {
                    0, true, 100, "hello world" // 10 th
                },
        };
        for (int i = 0; i < input.length; i++) {
            String s = input[i];
            Object[] args = parser.parse(s);
            for (int j = 0; j < values[i].length; j++) {
                Object value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

    @Test(expected = ParseException.class)
    public void test04ParseException() throws ParseException {
        List<Argument> arguments = ArgumentFactory.builder().build()
                .setName("-a")
                .setIsOptional()
                .append(int.class)
                .setName("-b")
                .setIsFlag()
                .append(boolean.class)
                .setIsPositional()
                .append(int.class)
                .setIsPositional()
                .append(String.class)
                .get();
        AbstractPatternGenerator<Collection<Argument>> generator = new ArgumentPatternGenerator();
        generator.setSource(arguments);
        AbstractParser<Collection<Argument>> parser = new ArgumentParser(arguments);
        Pattern pattern = generator.getPattern();
        String input = "-a 5 -b 'hello world' 100";
        Assert.assertTrue(allMatch(pattern, input));
        parser.parse(input);
    }
}
