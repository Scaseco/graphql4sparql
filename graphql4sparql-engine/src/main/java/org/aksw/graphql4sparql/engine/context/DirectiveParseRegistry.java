package org.aksw.graphql4sparql.engine.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A registry for directive parsers.
 */
public class DirectiveParseRegistry {
    private static DirectiveParseRegistry INSTANCE;

    /**
     * Returns the singleton instance of the registry.
     *
     * @return The registry instance
     */
    public static DirectiveParseRegistry get() {
        if (INSTANCE == null) {
            synchronized (DirectiveParseRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DirectiveParseRegistry();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * The registry of directive parsers.
     */
    protected Map<String, DirectiveParser<?>> registry;

    /**
     * Creates a new DirectiveParseRegistry.
     */
    public DirectiveParseRegistry() {
        super();
        this.registry = new LinkedHashMap<>();
    }

    /**
     * Registers a directive parser.
     *
     * @param <T> The type of the parsed result
     * @param parser The parser to register
     */
    public <T> void put(DirectiveParser<T> parser) {
        String name = parser.getName();
        registry.put(name, parser);
    }

    /**
     * Gets a directive parser by name.
     *
     * @param <T> The type of the parsed result
     * @param name The name of the parser
     * @return The directive parser
     */
    public <T> DirectiveParser<T> get(String name) {
        DirectiveParser<?> tmp = registry.get(name);
        if (tmp == null) {
            throw new NoSuchElementException("No parser for name: " + name);
        }

        @SuppressWarnings("unchecked")
        DirectiveParser<T> result = (DirectiveParser<T>)tmp;
        return result;
    }
}
