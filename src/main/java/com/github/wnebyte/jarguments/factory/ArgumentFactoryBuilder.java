package com.github.wnebyte.jarguments.factory;

import com.github.wnebyte.jarguments.adapter.AbstractTypeAdapterRegistry;
import com.github.wnebyte.jarguments.adapter.TypeAdapterRegistry;
import java.util.ArrayList;
import java.util.Collection;

public class ArgumentFactoryBuilder extends AbstractArgumentFactoryBuilder {

    private Collection<Character> exclude = new ArrayList<Character>() {{
        add(' ');
        add('"');
        add('\'');
    }};

    private AbstractTypeAdapterRegistry typeConverters = TypeAdapterRegistry.getInstance();

    public ArgumentFactoryBuilder excludeCharacters(Collection<Character> characters) {
        this.exclude = characters;
        return this;
    }

    public ArgumentFactoryBuilder useTypeConverterMap(AbstractTypeAdapterRegistry converters) {
        this.typeConverters = converters;
        return this;
    }

    public ArgumentFactory build() {
        return new ArgumentFactory(exclude, typeConverters);
    }
}