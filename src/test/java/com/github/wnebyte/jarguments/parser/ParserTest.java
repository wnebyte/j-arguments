package com.github.wnebyte.jarguments.parser;

import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.util.*;

public class ParserTest {

    @Test
    public void test00() throws ParseException  {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("-a", null, true,
                null, null, null, int.class);
        factory.create(null, null, true,
                null, null, null, int.class);
        Set<Argument> arguments = factory.getAll();
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
            TokenSequence tokens = TokenSequence.tokenize(input[i]);
            Parser parser = new Parser();
            Object[] vals = parser.parse("input", tokens, arguments);

            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, vals[j]);
            }
        }
    }

    @Test
    public void test01() throws ParseException {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("-a", null, false,
                null, null, null, int.class);
        factory.create(null, null, true,
                null, null, null, int.class);
        Set<Argument> arguments = factory.getAll();
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
            TokenSequence tokens = TokenSequence.tokenize(input[i]);
            Parser parser = new Parser();
            Object[] vals = parser.parse("input", tokens, arguments);

            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, vals[j]);
            }
        }
    }

    @Test
    public void test02() throws ParseException {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("-a", null, false,
                null, null, null, int.class);
        factory.create("-b", null, false,
                null, null, null, int.class);
        factory.create(null, null, true,
                null, null, null, int.class);
        Set<Argument> arguments = factory.getAll();
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
            TokenSequence tokens = TokenSequence.tokenize(input[i]);
            Parser parser = new Parser();
            Object[] vals = parser.parse("input", tokens, arguments);

            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, vals[j]);
            }
        }
    }

    @Test
    public void test03() throws ParseException {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("-a", null, false,
                null, null, null, int.class);
        factory.create("-b", null, false,
                null, null, null, boolean.class);
        factory.create(null, null, true,
                null, null, null, int.class);
        factory.create(null, null, true,
                null, null, null, int.class);
        Set<Argument> arguments = factory.getAll();
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
        TokenSequence tokens = TokenSequence.tokenize(input[0]);
        Parser parser = new Parser();
        Object[] vals = parser.parse("input", tokens, arguments);
        Assert.assertEquals(values[0][0], vals[0]);
        Assert.assertEquals(values[0][1], vals[1]);
        Assert.assertEquals(values[0][2], vals[2]);
        Assert.assertEquals(values[0][3], vals[3]);
    }

    @Test
    public void test04() throws ParseException {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("-a", null, false,
                null, null, null, int.class);
        factory.create("-b", null, false,
                null, null, null, boolean.class);
        factory.create(null, null, true,
                null, null, null, int.class);
        factory.create(null, null, true,
                null, null, null, String.class);
        Set<Argument> arguments = factory.getAll();
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
            TokenSequence tokens = TokenSequence.tokenize(input[i]);
            Parser parser = new Parser();
            Object[] vals = parser.parse("input", tokens, arguments);

            for (int j = 0; j < values[i].length; j++) {
                Object value = values[i][j];
                Assert.assertEquals(value, vals[j]);
            }
        }
    }

    @Test(expected = ParseException.class)
    public void test04ParseException() throws ParseException {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("-a", null, false,
                null, null, null, int.class);
        factory.create("-b", null, false,
                null, null, null, boolean.class);
        factory.create(null, null, true,
                null, null, null, int.class);
        factory.create(null, null, true,
                null, null, null, String.class);
        Set<Argument> arguments = factory.getAll();
        String input = "-a 5 -b 'hello world' 100";
        TokenSequence tokens = TokenSequence.tokenize(input);
        Parser parser = new Parser();
        Object[] vals = parser.parse("input", tokens, arguments);
    }

    @Test
    public void test05() throws ParseException {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("name, -n", null, true,
                null, null, null, String.class);
        factory.create("url, -u", null, true,
                null, null, null, String.class);
        factory.create("comment, -c", null, true,
                null, null, null, String.class);
        Set<Argument> arguments = factory.getAll();
        String input = "name 'Lars Petterson' url https://www.google.com -c \"this is a comment\"";
        Object[] values = {
                "Lars Petterson",
                "https://www.google.com",
                "this is a comment"
        };
        TokenSequence tokens = TokenSequence.tokenize(input);
        Parser parser = new Parser();
        Object[] vals = parser.parse("input", tokens, arguments);
        Assert.assertEquals(vals.length, values.length);
        for (int i = 0; i < vals.length; i++) {
            Assert.assertEquals(vals[i], values[i]);
        }
    }
}
