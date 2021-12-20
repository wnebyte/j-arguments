package com.github.wnebyte.jarguments.factory;

import java.util.Collection;
import com.github.wnebyte.jarguments.convert.AbstractTypeConverterMap;

public abstract class AbstractArgumentFactoryBuilder {

    public abstract AbstractArgumentFactoryBuilder excludeCharacters(Collection<Character> characters);

    public abstract AbstractArgumentFactoryBuilder useTypeConverterMap(AbstractTypeConverterMap converters);

    public abstract AbstractArgumentFactory build();
}
