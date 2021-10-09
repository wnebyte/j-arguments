package com.github.wnebyte.args;

import java.util.Arrays;
import java.util.Collection;

public class ArgumentFactoryBuilder {

    private Collection<Character> exclude
            = Arrays.asList(' ', '"', '\'');

    private AbstractTypeConverters typeConverters
            = TypeConverters.getInstance();

    public ArgumentFactoryBuilder exclude(final Collection<Character> exclude) {
        this.exclude = exclude;
        return this;
    }

    public ArgumentFactoryBuilder useTypeConverters(final AbstractTypeConverters typeConverterRepository) {
        this.typeConverters = typeConverterRepository;
        return this;
    }

    public ArgumentFactory build() {
        return new ArgumentFactory(exclude, typeConverters);
    }
}
