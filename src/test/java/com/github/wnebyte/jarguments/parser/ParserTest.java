package com.github.wnebyte.jarguments.parser;

import com.github.wnebyte.jarguments.AbstractTestClass;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.Tokens;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.factory.ArgumentFactory;
import com.github.wnebyte.jarguments.factory.ArgumentFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest extends AbstractTestClass {

    @Test
    public void test00() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsRequired()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .get();
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
            Tokens tokens = Tokens.tokenize(input[i]);
            Parser parser = new Parser();
            parser.parse(tokens, arguments);
            Object[] args = parser.initialize();

            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

    @Test
    public void test01() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsOptional()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .get();
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
            Tokens tokens = Tokens.tokenize(input[i]);
            Parser parser = new Parser();
            parser.parse(tokens, arguments);
            Object[] args = parser.initialize();

            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

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
            Tokens tokens = Tokens.tokenize(input[i]);
            Parser parser = new Parser();
            parser.parse(tokens, arguments);
            Object[] args = parser.initialize();

            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

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
        Object[][] values = {
                {
                        5, true, 100, 50
                }
        };
        Tokens tokens = Tokens.tokenize(input[0]);
        Parser parser = new Parser();
        parser.parse(tokens, arguments);
        Object[] args = parser.initialize();
        Assert.assertEquals(values[0][0], args[0]);
        Assert.assertEquals(values[0][1], args[1]);
        Assert.assertEquals(values[0][2], args[2]);
        Assert.assertEquals(values[0][3], args[3]);
    }

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
            Tokens tokens = Tokens.tokenize(input[i]);
            Parser parser = new Parser();
            parser.parse(tokens, arguments);
            Object[] args = parser.initialize();

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
        String input = "-a 5 -b 'hello world' 100";
        Tokens tokens = Tokens.tokenize(input);
        Parser parser = new Parser();
        parser.parse(tokens, arguments);
        parser.initialize(); // should throw an exception because the positional arguments are swapped.
    }
}
