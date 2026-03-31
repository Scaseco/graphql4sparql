package org.aksw.graphql4sparql.engine.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.binding.Binding;


/**
 * A view over a binding that remaps variables.
 * This class is useful in situations where the original variables were remapped to internal ones, and an internal
 * binding should appear as an original one to the application.
 */
public class BindingRemapped
    implements Binding
{
    /**
     * The delegate binding that this class wraps.
     */
    protected Binding delegate;

    /**
     * The mapping from variables to their remapped variables.
     */
    protected Map<Var, Var> varMap;

    // Computed when needed
    /**
     * The set of effective keys (variables that exist in both the map and delegate).
     */
    protected transient Set<Var> effectiveKeys;

    /**
     * Creates a new BindingRemapped instance.
     *
     * @param delegate The delegate binding to wrap
     * @param varMap The variable mapping to apply
     */
    protected BindingRemapped(Binding delegate, Map<Var, Var> varMap) {
        super();
        this.delegate = Objects.requireNonNull(delegate);
        this.varMap = Objects.requireNonNull(varMap);
    }

    /**
     * Creates a new BindingRemapped instance.
     *
     * @param delegate The delegate binding to wrap
     * @param varMap The variable mapping to apply
     * @return A new BindingRemapped instance
     */
    public static Binding of(Binding delegate, Map<Var, Var> varMap) {
        return new BindingRemapped(delegate, varMap);
    }

    /**
     * Returns the set of effective keys (variables that exist in both the map and delegate).
     *
     * @return The set of effective keys
     */
    protected Set<Var> effectiveKeys() {
        if (effectiveKeys == null) {
            effectiveKeys= varMap.keySet().stream().filter(delegate::contains).collect(Collectors.toSet());
        }
        return effectiveKeys;
    }

    @Override
    public Iterator<Var> vars() {
        return varMap.keySet().stream().filter(delegate::contains).iterator();
    }

    @Override
    public Set<Var> varsMentioned() {
        return effectiveKeys();
    }

    @Override
    public void forEach(BiConsumer<Var, Node> action) {
        varMap.forEach((v, w) -> {
            if (delegate.contains(w)) {
                action.accept(v, delegate.get(w));
            }
        });
    }

    @Override
    public boolean contains(Var var) {
        Var v = varMap.get(var);
        return delegate.contains(v);
    }

    /**
     * Maps the argument variable to the internal one.
     * Then uses the internal one for the lookup.
     */
    @Override
    public Node get(Var var) {
        Var v = varMap.get(var);
        Node result = v == null ? null : delegate.get(v);
        return result;
    }

    @Override
    public int size() {
        return (int)varMap.keySet().stream().filter(delegate::contains).count();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public String toString() {
        return delegate.toString() + ": " + varMap;
    }

    @Override
    public Binding detach() {
        Binding d = delegate == null ? null : delegate.detach();
        return d == delegate
            ? this
            : new BindingRemapped(d, varMap);
    }
}
