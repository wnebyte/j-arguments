package com.github.wnebyte.jarguments.util;

import java.util.*;

import org.junit.Test;
import org.junit.Assert;

public class StringsTest {

    @Test
    public void testFirstSubstring00() {
        String s = "--hej bananza";
        List<String> c = Arrays.asList("hej", "--hej", "---hej");
        String substring = Strings.firstSubstring(s, c);
        Assert.assertEquals(c.get(0), substring);
    }

    @Test
    public void testFirstSubstring01() {
        String s = "--a 100.5";
        List<String> c = Arrays.asList("-a", "--a");
        String substring = Strings.firstSubstring(s, c);
        Assert.assertEquals(c.get(0), substring);
    }

    @Test
    public void testFirstSubstring02() {
        String s = "--a 100.5";
        List<String> c = Arrays.asList("-a", "--a");
        String substring = Strings.firstSubstring(s, c);
        Assert.assertEquals(c.get(0), substring);
    }

    @Test
    public void testRemoveFirst00() {
        String s = "-a hello";
        String result = Strings.removeFirst(s, 'h');
        Assert.assertEquals("-a ello", result);
    }

    @Test
    public void testRemoveFirstAndLast00() {
        String s = "[1,2,3,4,5,6]";
        String result = Strings.removeFirstAndLast(s, '[', ']');
        Assert.assertEquals("1,2,3,4,5,6", result);
    }

    @Test
    public void testRemoveFirstAndLast01() {
        String s = "123[456]78";
        String result = Strings.removeFirstAndLast(s, '[', ']');
        Assert.assertEquals("12345678", result);
    }

    @Test
    public void testRemoveFirstAndLast02() {
        String s = "123[456[78";
        String result = Strings.removeFirstAndLast(s, '[', '[');
        Assert.assertEquals("12345678", result);
    }

    @Test
    public void testRemoveFirstAndLast03() {
        String s = "123[45678";
        String result = Strings.removeFirstAndLast(s, '[', '[');
        Assert.assertEquals(s, result);
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