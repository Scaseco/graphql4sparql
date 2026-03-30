package org.aksw.graphql4sparql.engine.context;

import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.lang3.ClassUtils;

import graphql.language.Directive;

/**
 * A default implementation of DirectiveParser.
 *
 * @param <T> The type of the parsed result
 */
public class DirectiveParserImpl<T>
    implements DirectiveParser<T>
{
    /**
     * The Java class of the parsed result.
     */
    protected final Class<? super T> javaClass;

    /**
     * The name of the directive.
     */
    protected final String name;

    /**
     * Whether the directive is unique.
     */
    protected final boolean isUnique;

    /**
     * The parser function.
     */
    protected Function<Directive, T> parser;

    /**
     * Creates a new DirectiveParserImpl.
     *
     * @param javaClass The Java class of the parsed result
     * @param name The name of the directive
     * @param isUnique Whether the directive is unique
     * @param parser The parser function
     */
    protected DirectiveParserImpl(Class<? super T> javaClass, String name, boolean isUnique, Function<Directive, T> parser) {
        super();
        this.javaClass = Objects.requireNonNull(javaClass);
        this.name = Objects.requireNonNull(name);
        this.isUnique = isUnique;
        this.parser = Objects.requireNonNull(parser);
    }

    /**
     * Creates a new DirectiveParser.
     *
     * @param <T> The type of the parsed result
     * @param javaClass The Java class of the parsed result
     * @param name The name of the directive
     * @param isUnqiue Whether the directive is unique
     * @param parser The parser function
     * @return A new DirectiveParser
     */
    public static <T> DirectiveParser<T> of(Class<? super T> javaClass, String name, boolean isUnqiue, Function<Directive, T> parser) {
        return new DirectiveParserImpl<>(javaClass, name, isUnqiue, parser);
    }

    /**
     * Returns the Java class of the parsed result.
     *
     * @return The Java class
     */
    public Class<? super T> getJavaClass() {
        return javaClass;
    }

    /**
     * Checks if this parser supports a given interface.
     *
     * @param interfaceToTestFor The interface to test
     * @return True if supported, false otherwise
     */
    public boolean supports(Class<?> interfaceToTestFor) {
        boolean result = ClassUtils.getAllInterfaces(javaClass).contains(interfaceToTestFor);
        return result;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public boolean isUnique() {
        return isUnique;
    }

    @Override
    public T parser(Directive directive) {
        return parser.apply(directive);
    }
}
