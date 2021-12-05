package com.github.wnebyte.jarguments.factory;

import com.github.wnebyte.jarguments.converter.AbstractTypeConverterMap;
import java.util.Collection;

public abstract class AbstractArgumentCollectionFactoryBuilder {

    protected abstract AbstractArgumentCollectionFactoryBuilder excludeCharacters(Collection<Character> characters);

    protected abstract AbstractArgumentCollectionFactoryBuilder useTypeConverterMap(AbstractTypeConverterMap converters);

    protected abstract AbstractArgumentCollectionFactory build();
}
