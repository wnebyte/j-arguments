package com.github.wnebyte.args.factory;

import com.github.wnebyte.args.converter.AbstractTypeConverterMap;
import java.util.Collection;

public abstract class AbstractArgumentFactoryBuilder {

    abstract AbstractArgumentFactoryBuilder excludeCharacters(Collection<Character> characters);

    abstract AbstractArgumentFactoryBuilder useTypeConverterMap(AbstractTypeConverterMap converters);

    abstract AbstractArgumentFactory build();
}
