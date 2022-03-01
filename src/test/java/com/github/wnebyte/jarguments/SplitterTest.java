package com.github.wnebyte.jarguments;

import org.junit.Test;
import org.junit.Assert;

public class SplitterTest {

    @Test
    public void test00() {
        String content = new Splitter()
                .setName("name")
                .setValue("name 'hello there'")
                .split()
                .normalize(true)
                .get();
        System.out.println(content);
    }

    @Test
    public void test01() {
        String value = new Splitter()
                .setName("-a")
                .setValue("-a 'hello there'")
                .split()
                .normalize(false)
                .get();
        Assert.assertEquals("hello there", value);
        value = new Splitter()
                .setName("-a")
                .setValue("-a \"hello 'there you'\"")
                .split()
                .normalize(false)
                .get();
        Assert.assertEquals("hello there you", value);
    }

    @Test
    public void test02() {
        String val = new Splitter()
                .setValue("[1,2,3,4,5]")
                .normalize(true)
                .get();
        Assert.assertEquals("1,2,3,4,5", val);
        val = new Splitter()
                .setValue("'some value'")
                .normalize(false)
                .get();
        Assert.assertEquals("some value", val);
    }

}
