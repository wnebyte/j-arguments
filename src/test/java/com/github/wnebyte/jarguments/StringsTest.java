package com.github.wnebyte.jarguments;

import com.github.wnebyte.jarguments.util.Strings;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringsTest {

    @Test
    public void test00() {
        String s = "--hej bananza ---hej";
        List<String> list = Arrays.asList("hej", "--hej", "---hej");
        String get = Strings.firstSubstring(s, list);
        System.out.println(get);
    }

    @Test
    public void test01() {
        String value = "--a 100.5";
        Set<String> names = new HashSet<>(Arrays.asList("-a", "--a"));
        boolean intersects = Strings.intersects(value, names);
        System.out.print(intersects);
    }

    @Test
    public void test02() {
        String s = "--a 100.5";
        Set<String> names = new HashSet<>(Arrays.asList("-a", "--a"));
        String str = Strings.firstSubstring(s, names);
        System.out.print(str);
    }

    @Test
    public void test03() {
        String s = "--a \"hello there\" --b 'hey there' -c test";
        List<String> elements = Strings.split(s, ' ', Arrays.asList('\'', '"'));
        List<String> expected = Arrays.asList("--a", "\"hello there\"", "--b", "'hey there'", "-c", "test");
        Assert.assertEquals(expected, elements);
    }

    @Test
    public void test04() {
        String s = "--a \"hello 'there you'\" --b 'hey there' -c test";
        List<String> elements = Strings.split(s, ' ', Arrays.asList('\'', '"'));
        System.out.println(Arrays.toString(elements.toArray()));
    }

    @Test
    public void test05() {
        String s = "-a \"hello 'there you 'yourself you''\" --b 'hey there' -c test";
        List<String> elements = Strings.split(s, ' ', Arrays.asList('\'', '"'));
        System.out.println(Arrays.toString(elements.toArray()));
    }

    @Test
    public void test06() {
        String s = "'hello' \"'there you go'\"";
        s = s.replaceAll("([\"]|['])", "");
        System.out.println(s);
    }

    @Test
    public void test07() {
        String input = "-a \"hello 'hello there'\"";
        List<String> elements = Strings.splitByWhitespace(input);
        System.out.println(Arrays.toString(elements.toArray()));
    }

    @Test
    public void test08() {
        String s = "-a \"'hello there 'test 'test test'test''\"";
        List<String> elements = Strings.splitByWhitespace(s);
        System.out.println(Arrays.toString(elements.toArray()));
    }
}