package com.github.wnebyte.jarguments.val;

import com.github.wnebyte.jarguments.AbstractTestClass;
import com.github.wnebyte.jarguments.Argument;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;

import com.github.wnebyte.jarguments.factory.ArgumentFactoryBuilder;

public class ArgumentValidatorTest extends AbstractTestClass {

    /*
    one REQUIRED,
    one POSITIONAL
     */
    @Test
    public void test00() {
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
        Assert.assertTrue(super.allMatch(arguments, input));
        input = new String[]{
                "-a 5",
                "100",
                "-a 5 100 ",
                " -a 5 100"
        };
        Assert.assertTrue(super.noneMatch(arguments, input));
    }

    /*
    one OPTIONAL,
    one POSITIONAL
     */
    @Test
    public void test01() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setName("-a")
                .setIsOptional()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .get();
        String[] input = {
                "-a 5 100",
                "100 -a 5",
                "100"
        };
        Assert.assertTrue(super.allMatch(arguments, input));
        input = new String[] {
                "-a 5 100 ",
                " -a 5 100",
                "-a 5"
        };
        Assert.assertTrue(super.noneMatch(arguments, input));
    }

    /*
    two OPTIONAL,
    one POSITIONAL
    */
    @Test
    public void test02() {
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
        Assert.assertTrue(super.allMatch(arguments, input));
        input = new String[] {
                "-a 5 -b 10 -c 100",
                "-a 5 -b 10",
                " -a 50 -b 10 100",
                "-a 50 -b 10 100 ",
                "-a 5",
                "-b 100"
        };
        Assert.assertTrue(super.noneMatch(arguments, input));
    }

    /*
    one OPTIONAL,
    one FLAG,
    two POSITIONAL
    */
    @Test
    public void test03() {
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
        Assert.assertTrue(super.allMatch(arguments, input));
        input = new String[] {
                "-a 5 -c 100 50",
                "-c 100 50",
                " 100 50",
                "100 50 -c ",
                "100 -b -a 10",
                "-a 10 -b 100",
                "-a 10 50 -b"
        };
        Assert.assertTrue(super.noneMatch(arguments, input));
    }

    /*
    two POSITIONAL
     */
    @Test
    public void test04() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setIsPositional()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .get();
        String[] input = {
                "500 100",
                "100 500",
                "100 500"
        };
        Assert.assertTrue(super.allMatch(arguments, input));
        input = new String[]{
                "",
                "100",
                "500",
                " 100 500",
                "500 100 "
        };
        Assert.assertTrue(super.noneMatch(arguments, input));
    }

    /*
    two POSITIONAL
    one REQUIRED
     */
    @Test
    public void test05() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setIsPositional()
                .append(int.class)
                .setIsPositional()
                .append(int.class)
                .setName("-r")
                .setIsRequired()
                .append(int.class)
                .get();
        String[] input = {
                "100 50 -r 5",
                "50 100 -r 5",
                "50 -r 5 100",
                "-r 5 50 100",
                "-r 5 100 50"
        };
        Assert.assertTrue(super.allMatch(arguments, input));
        input = new String[]{
                "-r 50",
                "-r 50 100",
                "100 500",
                "",
                " -r 50 100 50",
                "-r 50 100 50 "
        };
        Assert.assertTrue(super.noneMatch(arguments, input));
    }

    /*
   one POSITIONAL
   one POSITIONAL[]
   one REQUIRED
    */
    @Test
    public void test06() {
        List<Argument> arguments = new ArgumentFactoryBuilder().build()
                .setIsPositional()
                .append(int.class)
                .setIsPositional()
                .append(int[].class)
                .setName("-r")
                .setIsRequired()
                .append(int.class)
                .get();
        String[] input = {
                "100 [20,30] -r 5",
                "50 -r 5 [20,30]",
                "-r 5 100 [20,30]",
        };
        Assert.assertTrue(allMatch(arguments, input));
        input = new String[] {
                "[20,30] 100 -r 5",
                "-r 5 [20,30] 100",
                "[20,30] 100",
                "[20,30] -r 50"
        };
        Assert.assertTrue(noneMatch(arguments, input));
    }

}
