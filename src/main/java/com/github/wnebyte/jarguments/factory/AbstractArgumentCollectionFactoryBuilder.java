package com.github.wnebyte.jarguments.factory;

import java.util.Collection;
import com.github.wnebyte.jarguments.converter.AbstractTypeConverterMap;

public abstract class AbstractArgumentCollectionFactoryBuilder {

    public abstract AbstractArgumentCollectionFactoryBuilder excludeCharacters(Collection<Character> characters);

    public abstract AbstractArgumentCollectionFactoryBuilder useTypeConverterMap(AbstractTypeConverterMap converters);

    public abstract AbstractArgumentCollectionFactory build();
}
