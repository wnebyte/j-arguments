package com.github.wnebyte.jarguments.factory;

import com.github.wnebyte.jarguments.converter.AbstractTypeConverterMap;
import com.github.wnebyte.jarguments.converter.TypeConverterMap;
import java.util.ArrayList;
import java.util.Collection;

public class ArgumentCollectionFactoryBuilder extends AbstractArgumentCollectionFactoryBuilder {

    private Collection<Character> exclude = new ArrayList<Character>() {{
        add(' ');
        add('"');
        add('\'');
    }};

    private AbstractTypeConverterMap typeConverters = TypeConverterMap.getInstance();

    public ArgumentCollectionFactoryBuilder excludeCharacters(Collection<Character> characters) {
        this.exclude = characters;
        return this;
    }

    public ArgumentCollectionFactoryBuilder useTypeConverterMap(AbstractTypeConverterMap converters) {
        this.typeConverters = converters;
        return this;
    }

    public ArgumentCollectionFactory build() {
        return new ArgumentCollectionFactory(exclude, typeConverters);
    }
}