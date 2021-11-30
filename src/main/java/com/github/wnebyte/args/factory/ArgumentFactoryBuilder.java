package com.github.wnebyte.args.factory;

import com.github.wnebyte.args.converter.AbstractTypeConverterMap;
import com.github.wnebyte.args.converter.TypeConverterMap;
import java.util.ArrayList;
import java.util.Collection;

public class ArgumentFactoryBuilder extends AbstractArgumentFactoryBuilder {

    private Collection<Character> exclude = new ArrayList<Character>() {{
        add(' ');
        add('"');
        add('\'');
    }};

    private AbstractTypeConverterMap typeConverters = TypeConverterMap.getInstance();

    public ArgumentFactoryBuilder excludeCharacters(Collection<Character> characters) {
        this.exclude = characters;
        return this;
    }

    public ArgumentFactoryBuilder useTypeConverterMap(AbstractTypeConverterMap converters) {
        this.typeConverters = converters;
        return this;
    }

    public ArgumentFactory build() {
        return new ArgumentFactory(exclude, typeConverters);
    }
}