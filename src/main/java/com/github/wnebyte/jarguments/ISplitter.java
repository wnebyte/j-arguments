package com.github.wnebyte.jarguments;

public interface ISplitter {

    ISplitter setName(String name);

    ISplitter setValue(String value);

    ISplitter split();

    ISplitter normalize(boolean isArray);

    String get();
}
