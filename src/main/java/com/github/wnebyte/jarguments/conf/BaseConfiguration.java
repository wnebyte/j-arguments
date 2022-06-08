package com.github.wnebyte.jarguments.conf;

import java.util.Map;
import java.util.HashMap;
import com.github.wnebyte.jarguments.formatter.Formatter;
import com.github.wnebyte.jarguments.exception.ParseException;

/**
 * This class represents a base configuration.
 */
public class BaseConfiguration {

    /*
    ###########################
    #          FIELDS         #
    ###########################
    */

    private final Map<Class<? extends ParseException>, Formatter<? extends ParseException>> formatters
            = new HashMap<>();

    /*
    ###########################
    #          METHODS        #
    ###########################
    */

    public <T extends ParseException> void setFormatter(Class<T> cls, Formatter<T> formatter) {
        if (cls != null && formatter != null) {
            formatters.put(cls, formatter);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ParseException> Formatter<T> getFormatter(Class<T> cls) {
        if (cls != null) {
            if (formatters.containsKey(cls)) {
                Formatter<?> formatter = formatters.get(cls);
                return (Formatter<T>) formatter;
            }
        }
        return null;
    }

    public boolean hasFormatter(Class<?> cls) {
        return formatters.containsKey(cls);
    }
}
