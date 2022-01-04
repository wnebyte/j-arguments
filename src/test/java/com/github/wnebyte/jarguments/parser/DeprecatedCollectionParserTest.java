package com.github.wnebyte.jarguments.parser;

import com.github.wnebyte.jarguments.AbstractTestClass;
import com.github.wnebyte.jarguments.Argument;
import com.github.wnebyte.jarguments.pattern.DeprecatedCollectionPatternGenerator;
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.github.wnebyte.jarguments.exception.ParseException;
import com.github.wnebyte.jarguments.factory.ArgumentFactoryBuilder;

public class DeprecatedCollectionParserTest extends AbstractTestClass {

    @Test
    public void test00() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a", "-A")
                .setIsFlag()
                .append(boolean.class)
                .setName("-b", "-B")
                .setIsOptional()
                .setDefaultValue("99")
                .append(int.class)
                .get();
        String input = "-a -b 100";
        Pattern pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(pattern.matcher(input).matches());
        new DeprecatedCollectionParser(arguments).parse(input);
    }

    @Test
    public void testSuccessfulParseEmptyCollection() throws ParseException {
        List<Argument> arguments = new ArrayList<>();
        String input = "";
        new DeprecatedCollectionParser(arguments).parse(input);
    }

    @Test
    public void testSuccessfulParseMultipleFlag() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsFlag()
                .append(boolean.class)
                .setName("-b")
                .setIsFlag()
                .append(boolean.class)
                .setName("-c")
                .setIsFlag()
                .append(boolean.class)
                .get();
        String[] input = {
                "-a -b -c",
                "-b -a -c",
                "-c -b -a",
                "-c -a -b",
                "-b -c -a",
                "-a -b",
                "-b -a",
                "-c -a",
                "-c -b",
                "-a",
                "-b",
                "-c"
        };
        Pattern pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(allMatch(pattern, input));
        for (String s : input) {
            new DeprecatedCollectionParser(arguments).parse(s);
        }
    }

    @Test
    public void testSuccessfulParseFlagWithValues() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsFlag()
                .setFlagValue("10")
                .setDefaultValue("0")
                .append(int.class)
                .get();
        String input = "";
        Pattern pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(pattern.matcher(input).matches());
        int arg = (int) new DeprecatedCollectionParser(arguments).parse(input)[0];
        Assert.assertEquals(0, arg);
        input = "-a";
        pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(pattern.matcher(input).matches());
        arg = (int) new DeprecatedCollectionParser(arguments).parse(input)[0];
        Assert.assertEquals(10, arg);
    }

    @Test
    public void testSuccessfulParseMultiplePositional() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setIsPositional()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .get();
        String[] input = {
                "10 20 30",
                "20 10 30",
                "30 20 10",
                "10 30 20",
                "20 30 10",
                "30 10 20",
        };
        int[][] values = {
                new int[] {
                        10, 20, 30
                },
                new int[] {
                        20, 10, 30
                },
                new int[] {
                        30, 20, 10
                },
                new int[] {
                        10, 30, 20
                },
                new int[] {
                        20, 30, 10
                },
                new int[] {
                        30, 10, 20
                }
        };
        Pattern pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(allMatch(pattern, input));
        for (int i = 0; i < input.length; i++) {
            String s = input[i];
            Object[] args = new DeprecatedCollectionParser(arguments).parse(s);
            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

    @Test
    public void testSuccessfulParseMultipleRequired() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsRequired()
                .append(int.class)
                .setName("-b")
                .setIsRequired()
                .append(int.class)
                .setName("-c")
                .setIsRequired()
                .append(int.class)
                .get();
        String[] input = {
                "-a 10 -b 20 -c 30",
                "-b 20 -a 10 -c 30",
                "-c 30 -b 20 -a 10",
                "-c 30 -a 10 -b 20",
                "-b 20 -c 30 -a 10",
        };
        int[][] values = {
                new int[] {
                        10, 20, 30
                },
                new int[] {
                        10, 20, 30
                },
                new int[] {
                        10, 20, 30
                },
                new int[] {
                        10, 20, 30
                },
                new int[] {
                        10, 20, 30
                }
        };
        Pattern pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(allMatch(pattern, input));
        for (int i = 0; i < input.length; i++) {
            String s = input[i];
            Object[] args = new DeprecatedCollectionParser(arguments).parse(s);
            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

    @Test
    public void testSuccessfulParseMultipleOptional00() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsOptional()
                .append(int.class)
                .setName("-b")
                .setIsOptional()
                .append(int.class)
                .setName("-c")
                .setIsOptional()
                .append(int.class)
                .get();
        String[] input = {
                "-a 10 -b 20 -c 30",
                "-b 20 -a 10 -c 30",
                "-c 30 -b 20 -a 10",
                "-c 30 -a 10 -b 20",
                "-b 20 -c 30 -a 10",
        };
        int[][] values = {
                new int[] {
                        10, 20, 30
                },
                new int[] {
                        10, 20, 30
                },
                new int[] {
                        10, 20, 30
                },
                new int[] {
                        10, 20, 30
                },
                new int[] {
                        10, 20, 30
                }
        };
        Pattern pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(allMatch(pattern, input));
        for (int i = 0; i < input.length; i++) {
            String s = input[i];
            Object[] args = new DeprecatedCollectionParser(arguments).parse(s);
            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

    @Test
    public void testSuccessfulParseMultipleOptional01() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsOptional()
                .append(int.class)
                .setName("-b")
                .setIsOptional()
                .append(int.class)
                .setName("-c")
                .setIsOptional()
                .append(int.class)
                .get();
        String[] input = {
                "-a 20",
                "-b 20",
                "-c 20",
                "-a 20 -b 20",
                "-a 20 -b 20 -c 20",
                "-b 20 -c 20",
                "-b 20 -c 20 -a 20",
                "-c 20 -b 20 -a 20",
                "-c 20 -b 20"
        };
        int[][] values = {
                new int[] {
                        20, 0, 0
                },
                new int[] {
                        0, 20, 0
                },
                new int[] {
                        0, 0, 20
                },
                new int[] {
                        20, 20, 0
                },
                new int[] {
                        20, 20, 20
                },
                new int[] {
                        0, 20, 20
                },
                new int[] {
                        20, 20, 20
                },
                new int[] {
                        20, 20, 20
                },
                new int[] {
                        0, 20, 20
                }
        };
        Pattern pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(allMatch(pattern, input));
        for (int i = 0; i < input.length; i++) {
            String s = input[i];
            Object[] args = new DeprecatedCollectionParser(arguments).parse(s);
            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                Assert.assertEquals(value, args[j]);
            }
        }
    }

    @Test
    public void testSuccessfulParse00() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setIsPositional()
                .append(String.class)
                .setName("-b")
                .setIsOptional()
                .append(int.class)
                .setName("-c")
                .setIsFlag()
                .append(boolean.class)
                .get();
        String[] input = {
                "'hello there' -b 100 -c",
                "'hello there' -c",
                "'hello there' -b 100",
                "'hello there' -c -b 100",
                "'hello there'"
        };
        Pattern pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(allMatch(pattern, input));
        for (String s : input) {
            new DeprecatedCollectionParser(arguments).parse(s);
        }
    }

    @Test
    public void testSuccessfulParse01() throws ParseException {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setIsPositional()
                .append(String.class)
                .setName("-b")
                .setIsRequired()
                .append(int.class)
                .setName("-c")
                .setIsFlag()
                .append(boolean.class)
                .setName("-d")
                .setIsOptional()
                .append(int.class)
                .get();
        String[] input = {
                "'hello there' -b 100 -c -d 500",
                "'hello there' -b 100 -c",
                "'hello there' -b 100",
                "'hello there' -d 500 -b 100",
                "'hello there' -b 100 -d 500 -c",
                "'hello there' -c -d 500 -b 200"
        };
        Pattern pattern = new DeprecatedCollectionPatternGenerator(arguments).getPattern();
        Assert.assertTrue(allMatch(pattern, input));
        for (String s : input) {
            new DeprecatedCollectionParser(arguments).parse(s);
        }
    }
}
