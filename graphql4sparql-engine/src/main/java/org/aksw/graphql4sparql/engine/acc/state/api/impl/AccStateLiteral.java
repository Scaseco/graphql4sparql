package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateBase;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeProduceNode;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;
import org.aksw.graphql4sparql.engine.io.ObjectNotationWriter;

/**
 * AccStateLiteral:
 * Extracts a value from the input and forwards it to the writer.
 * ValueParent must be 'entry' or 'array.
 *
 * Always accepts the current state. (Matches the same stateId as the parent)
 * Never transitions into a new state.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AccStateLiteral<I, E, K, V>
    extends AccStateBase<I, E, K, V>
    implements AccStateTypeProduceNode<I, E, K, V>
    // implements AccLateralNode<D, C, O>
{
    /** Function to extract the value from input. */
    protected BiFunction<I, E, ? extends V> inputToValue;
    /** The current value. */
    protected V currentValue;

    /**
     * Creates a new AccStateLiteral.
     *
     * @param inputToValue Function to extract the value from input
     */
    protected AccStateLiteral(BiFunction<I, E, ? extends V> inputToValue) {
        this.inputToValue = Objects.requireNonNull(inputToValue);
    }

    @Override
    public GonType getGonType() {
        return GonType.LITERAL;
    }

//    @Override
//    public AccStateGon<I, E, K, V> getParent() {
//        return (AccStateTypeNonObject<I, E, K, V>)super.getParent();
//    }

    /**
     * Creates a new AccStateLiteral.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param inputToValue Function to extract the value from input
     * @return A new AccStateLiteral instance
     */
    public static <I, E, K, V> AccStateLiteral<I, E, K, V> of(BiFunction<I, E, ? extends V> inputToValue) {
        return new AccStateLiteral<>(inputToValue);
    }

    @Override
    protected void beginActual() throws IOException {
        currentValue = inputToValue.apply(parentInput, null);
    }

    @Override
    public AccStateGon<I, E, K, V> transitionActual(Object inputStateId, I input, E env) {
        // Literals reject all edges (indicated by null)
        return null;
    }

    @Override
    public void endActual() throws IOException {
        if (!skipOutput) {
            // O effectiveValue = value == null ? new RdfNull() : value;
            //O effectiveValue = value;

            if (context.isSerialize()) {
                ObjectNotationWriter<K, V> writer = context.getJsonWriter();
                writer.value(currentValue);
            }

            if (context.isMaterialize()) {
            }
//            if (context.isMaterialize() && parent != null) {
//                //parent.acceptContribution(effectiveValue, context);
//            }
        }
        // super.end(context);
    }

    @Override
    public String toString() {
        return "AccStateLiteral(source: " + currentSourceNode + ")";
    }

    @Override
    public Iterator<? extends AccStateGon<I, E, K, V>> children() {
        return Collections.emptyIterator();
    }
}
