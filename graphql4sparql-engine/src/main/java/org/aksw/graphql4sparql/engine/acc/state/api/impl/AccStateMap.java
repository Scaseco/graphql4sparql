package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateBase;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeProduceEntry;

/**
 * This mapper only generates the keys - it does not generate the enclosing start/end object tags.
 *
 * <pre>{@code
 * {
 *   k1: [k1v1, k1v2]
 *   k2: k2v
 * }
 * }</pre>
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AccStateMap<I, E, K, V>
    extends AccStateBase<I, E, K, V>
    implements AccStateTypeProduceEntry<I, E, K, V>
{
    // AccStateTypeProduceObject
    // protected AccStateGon<I, E, K, V> valueParent;

    /** Function to map input and environment to key. */
    protected BiFunction<I, E, ? extends K> inputToKeyMapper;
    /** Predicate to test if the value is single. */
    protected BiPredicate<I, E> testIfSingle;
    /** The sub-accumulator. */
    protected AccStateGon<I, E, K, V> subAcc;

    /** The current key. */
    protected Object currentKey;
    /** Whether the current key is single. */
    protected boolean isCurrentKeySingle;

    /** Count of targets seen since last call to begin(). */
    protected int seenTargetCount = 0;
    /** Whether skip output has started here. */
    protected boolean skipOutputStartedHere = false;

    /** The match state id. */
    protected Object matchStateId;

    // public AccStateMap(Object matchStateId, BiFunction<I, E, ? extends K> inputToKeyMapper, BiPredicate<I, E> testIfSingle, AccStateTypeProduceNode<I, E, K, V> subAcc) {
    /**
     * Creates a new AccStateMap.
     *
     * @param matchStateId The match state id
     * @param inputToKeyMapper Function to map input and environment to key
     * @param testIfSingle Predicate to test if the value is single
     * @param subAcc The sub-accumulator
     */
    public AccStateMap(Object matchStateId, BiFunction<I, E, ? extends K> inputToKeyMapper, BiPredicate<I, E> testIfSingle, AccStateGon<I, E, K, V> subAcc) {
        super();
        this.matchStateId = matchStateId;
        this.inputToKeyMapper = Objects.requireNonNull(inputToKeyMapper);
        this.testIfSingle = testIfSingle;
        this.subAcc = subAcc; // TODO Validate gon type = produce node
    }

//    @Override
//    public AccStateTypeProduceObject<I, E, K, V> getParent() {
//        return (AccStateTypeProduceObject<I, E, K, V>)super.getParent();
//    }
//    @Override
//    public AccStateTypeProduceObject<I, E, K, V> getValueParent() {
//        return valueParent;
//    }

    @Override
    public AccStateGon<I, E, K, V> transitionActual(Object inputStateId, I input, E env) throws IOException {
        AccStateGon<I, E, K, V> result;

        if (!Objects.equals(inputStateId, matchStateId)) {
            result = null;
        } else {
            K key = inputToKeyMapper.apply(input, env);

            if (!Objects.equals(key, currentKey)) {

                // End a previously started array
                endOpenArray();

                // Reset counter
                seenTargetCount = 0;
                skipOutputStartedHere = false;

                currentKey = key;

//                if (subAcc.hasBegun()) {
//                    subAcc.end();
//                }

                if (!skipOutput) {
                    if (context.isSerialize()) {
                        context.getJsonWriter().name(key);
                    }
                }

                isCurrentKeySingle = testIfSingle.test(input, env);

                if (!isCurrentKeySingle) {
                    if (!skipOutput) {
                        if (context.isSerialize()) {
                            context.getJsonWriter().beginArray();
                        }
                    }
                }

            }


            skipOutputStartedHere = isCurrentKeySingle && seenTargetCount >= 1;
            subAcc.begin(key, input, env, skipOutput || skipOutputStartedHere);
            ++seenTargetCount;

            result = subAcc;
        }
        return result;
    }

    /**
     * Ends the currently open array.
     *
     * @throws IOException If an I/O error occurs
     */
    protected void endOpenArray() throws IOException {
        if (currentKey != null) {
            if (!isCurrentKeySingle) {
                if (!skipOutput) {
                    if (context.isSerialize()) {
                        context.getJsonWriter().endArray();
                    }
                }
            }
            currentKey = null;
        }
    }

    @Override
    public void endActual() throws IOException {
//        if (subAcc.hasBegun()) {
//            subAcc.end();
//        }
        endOpenArray();
//        if (!skipOutput) {
//            // O effectiveValue = value == null ? new RdfNull() : value;
//            O effectiveValue = value;
//
//            if (context.isSerialize()) {
//                RdfObjectNotationWriter writer = context.getJsonWriter();
//                if (value == null) {
//                    writer.nullValue();
//                } else {
//                    // writer.value(value.getAsLiteral().getInternalId());
//                    throw new RuntimeException("fix implementation");
//                }
//            }
//
//            if (context.isMaterialize() && parent != null) {
//                parent.acceptContribution(effectiveValue, context);
//            }
//        }
    }

    @Override
    public String toString() {
        return "AccStateMap(matches: " + matchStateId + ", source: " + currentSourceNode + ", target: " + subAcc + ")";
    }

    @Override
    public Iterator<? extends AccStateGon<I, E, K, V>> children() {
        return List.of(subAcc).iterator();
    }

    @Override
    public Object getMatchStateId() {
        return matchStateId;
    }
}
