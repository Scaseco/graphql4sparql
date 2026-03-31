package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateBase;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeNonObject;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeProduceNode;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * AccState for yielding a sequence of items without enclosing them in an array;
 * because the array would be a single object-notation entity on its own.
 * This is needed for streaming the individual items of a multi-valued top-level field.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AccStateArrayInit<I, E, K, V>
    extends AccStateBase<I, E, K, V>
    implements AccStateTypeNonObject<I, E, K, V>
{
    /** The match state id for AccJsonObject to index AccJsonEdge by this attribute. */
    protected Object matchStateId; // AccJsonObject should index AccJsonEdge by this attribute

    // protected Node currentTarget = null;
    /** The target accumulator. */
    protected AccStateTypeProduceNode<I, E, K, V> targetAcc;

    /** Count of targets seen since last call to begin(). */
    protected long seenTargetCount = 0;

    // public AccJsonProperty(TupleBridge3<Binding, Node> tripleAccessor, P_Path0 jsonKey, Node matchFieldId, boolean isForward, AccJsonNode targetAcc, boolean isSingle) {
    /**
     * Creates a new AccStateArrayInit.
     *
     * @param matchStateId The match state id
     * @param targetAcc The target accumulator
     */
    public AccStateArrayInit(Object matchStateId, AccStateTypeProduceNode<I, E, K, V> targetAcc) {
        super();
        this.matchStateId = matchStateId;
        this.targetAcc = targetAcc;
    }

    @Override
    public GonType getGonType() {
        return GonType.ARRAY;
    }

    //@Override
    /**
     * Gets the match state id.
     *
     * @return The match state id
     */
    public Object getMatchStateId() {
        return matchStateId;
    }

    /**
     * Sets the source node which subsequent triples must match in addition to the fieldId.
     * This method should be called by the owner of the edge such as AccJsonObject.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void beginActual() throws IOException {
        seenTargetCount = 0;
        if (!skipOutput && context.isSerialize()) {
            // context.getJsonWriter().beginArray();
        }
    }

    /**
     * Accepts a triple if source and field id match that of the current state.
     *
     * @param inputStateId The input state id
     * @param input The input
     * @param env The environment
     * @return The new state after transition, or null if no transition occurred
     * @throws IOException If an I/O error occurs
     */
    @Override
    public AccStateGon<I, E, K, V> transitionActual(Object inputStateId, I input, E env) throws IOException {
        AccStateGon<I, E, K, V> result = null;
        // Object inputStateId = getInputStateId(input, env);
        if (Objects.equals(matchStateId, inputStateId)) {
            // if (Objects.equals(currentSourceNode, edgeInputSource)) {
                ++seenTargetCount;

               //  currentTarget = isForward ? o : s; // TripleUtils.getTarget(input, isForward);
                targetAcc.begin(null, input, env, skipOutput);
                result = targetAcc;
            // }
        }
        return result;
    }

    @Override
    public void endActual() throws IOException {
        if (!skipOutput && context.isSerialize()) {
            // context.getJsonWriter().endArray();
        }
    }

    @Override
    public String toString() {
        return "AccStateArrayInit(matches: " + matchStateId + ", currentInput: " + currentInput + ", " + targetAcc + ")";
    }

    @Override
    public Iterator<? extends AccStateGon<I, E, K, V>> children() {
        return List.of(targetAcc).iterator();
    }
}
