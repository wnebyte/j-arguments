package com.github.wnebyte.jarguments.formatter;

import com.github.wnebyte.jarguments.ContextView;
import com.github.wnebyte.jarguments.Formatter;
import com.github.wnebyte.jarguments.HelpFormatter;
import com.github.wnebyte.jarguments.util.ArgumentFactory;
import com.github.wnebyte.jarguments.adapter.TypeAdapterRegistry;
import com.github.wnebyte.jarguments.util.Sets;

import java.util.ArrayList;
import java.util.List;

public class HelpFormatterTest {

    public static void main(String[] args) {
        HelpFormatterTest test = new HelpFormatterTest();
        test.multipleTest01();
    }

    private static final TypeAdapterRegistry adapters
            = TypeAdapterRegistry.getInstance();

    private static Formatter<ContextView> formatter
            = HelpFormatter.get();

    public void requiredAndOptionalTest00() {
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
        String s = formatter.apply(ContextView.of("name", "This is a description", factory.getAll()));
        System.out.println(s);
        System.out.println("nxt line");
    }

    public void requiredTest00() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create(null, null, true,
                null, "DATA", null, String.class);
        factory.create("-important", null, true,
                null, null, null, String.class);
        factory.create("-req", null, true,
                null, null, null, String.class);
        factory.create(null, null, true,
                null, "PATH", null, String.class);
        String s = formatter.apply(ContextView.of("name", "This is a description", factory.getAll()));
        System.out.println(s);
        System.out.println("nxt line");
    }

    public void optionalTest00() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("-v, --verbose", null, false,
                null, null, null, boolean.class);
        factory.create("-s, --stacktrace", null, false,
                null, null, null, boolean.class);
        String s = formatter.apply(ContextView.of("name", "This is a description", factory.getAll()));
        System.out.println(s);
        System.out.println("nxt line");
    }

    public void multipleTest00() {
        List<ContextView> views = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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
            views.add(ContextView.of("desc", factory.getAll()));
        }

        for (ContextView view : views) {
            String s = formatter.apply(view);
            System.out.println(s);
        }
    }

    public void multipleTest01() {
        List<ContextView> views = new ArrayList<>();
        views.add(ContextView.of("name", "desc", Sets.newSet()));
        views.add(getRequiredAndOptional());
        views.add(ContextView.of("name", null, Sets.newSet()));
        views.add(getRequired());
        views.add(ContextView.of("name", "desc", Sets.newSet()));
        views.add(getOptional());
        views.add(ContextView.of("name", "desc", Sets.newSet()));
        views.add(getRequiredAndOptional());

        for (ContextView view : views) {
            String s = formatter.apply(view);
            System.out.println(s);
        }

        System.out.println("end");
    }

    private ContextView getRequiredAndOptional() {
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
        return ContextView.of("name", "This is a description", factory.getAll());
    }

    private ContextView getRequired() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create(null, null, true,
                null, "DATA", null, String.class);
        factory.create("-important", null, true,
                null, null, null, String.class);
        factory.create("-req", null, true,
                null, null, null, String.class);
        factory.create(null, null, true,
                null, "PATH", null, String.class);
        return ContextView.of("name", "This is a description", factory.getAll());
    }

    private ContextView getOptional() {
        ArgumentFactory factory = new ArgumentFactory();
        factory.create("-v, --verbose", null, false,
                null, null, null, boolean.class);
        factory.create("-s, --stacktrace", null, false,
                null, null, null, boolean.class);
        return ContextView.of("name", "This is a description", factory.getAll());
    }
}
