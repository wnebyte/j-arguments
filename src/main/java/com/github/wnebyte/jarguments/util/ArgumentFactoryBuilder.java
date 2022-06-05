package com.github.wnebyte.jarguments.util;

import java.util.Collection;
import com.github.wnebyte.jarguments.adapter.AbstractTypeAdapterRegistry;

public class ArgumentFactoryBuilder {

    private AbstractTypeAdapterRegistry typeAdapterRegistry = null;

    private Collection<Character> excludeCharacters = null;

    private Collection<String> excludeNames = null;

    public ArgumentFactoryBuilder setTypeAdapterRegistry(AbstractTypeAdapterRegistry typeAdapterRegistry) {
        this.typeAdapterRegistry = typeAdapterRegistry;
        return this;
    }

    public ArgumentFactoryBuilder setExcludeCharacters(Collection<Character> excludeCharacters) {
        this.excludeCharacters = excludeCharacters;
        return this;
    }

    public ArgumentFactoryBuilder setExcludeNames(Collection<String> excludeNames) {
        this.excludeNames = excludeNames;
        return this;
    }

    public ArgumentFactory build() {
        return new ArgumentFactory(typeAdapterRegistry, excludeCharacters, excludeNames);
    }
}
