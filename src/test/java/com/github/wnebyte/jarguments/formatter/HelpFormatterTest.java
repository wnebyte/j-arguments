package com.github.wnebyte.jarguments.formatter;

import com.github.wnebyte.jarguments.util.ArgumentFactory;
import com.github.wnebyte.jarguments.adapter.TypeAdapterRegistry;

public class HelpFormatterTest {

    public static void main(String[] args) {
        HelpFormatterTest test = new HelpFormatterTest();
        test.test00();
    }

    private static final TypeAdapterRegistry adapters
            = TypeAdapterRegistry.getInstance();

    public void test00() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create(null, null, true,
                null, "DATA", null, String.class);
        factory.create("-important", null, true,
                null, null, null, String.class);
        factory.create("-req", null, true,
                null, null, null, String.class);
        factory.create(null, null, true,
                null, "PATH", null, String.class);
        factory.create("-v, --verbose", null, false,
                null, null, null, boolean.class);
        factory.create("-s, --stacktrace", null, false,
                null, null, null, boolean.class);
        String s = HelpFormatter.get().apply(factory.getAll());
        System.out.println(s);
    }
}
