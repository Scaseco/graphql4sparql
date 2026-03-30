package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateBase;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeTransition;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Transition to a sub-acc state if a condition is met. Used for fragments.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AccStateCondition<I, E, K, V>
    extends AccStateBase<I, E, K, V>
    implements AccStateTypeTransition<I, E, K, V>
{
    /** The match field id for AccJsonObject to index AccJsonEdge by this attribute. */
    protected Object matchFieldId; // AccJsonObject should index AccJsonEdge by this attribute

//    protected Node currentTarget = null;
    /** The sub-accumulator to transition to. */
    protected AccStateGon<I, E, K, V> subAcc;
//    protected boolean skipOutputStartedHere = false;

    // since last call to begin()
    // protected long seenTargetCount = 0;

    /** If true then no array is created. Any item after the first raises an error event. */
    // protected boolean isSingle = false;

    /**
     * Creates a new AccStateCondition.
     *
     * @param matchFieldId The match field id
     * @param subAcc The sub-accumulator to transition to
     */
    public AccStateCondition(Object matchFieldId, AccStateGon<I, E, K, V> subAcc) {
        super();
        this.matchFieldId = matchFieldId;
        this.subAcc = subAcc;
    }

    @Override
    public GonType getGonType() {
        return subAcc.getGonType();
    }

    @Override
    public Object getMatchStateId() {
        return matchFieldId;
    }

    @Override
    public AccStateGon<I, E, K, V> transitionActual(Object inputStateId, I input, E env) throws IOException {
        AccStateGon<I, E, K, V> result = null;
        if (matchFieldId.equals(inputStateId)) {
            // currentTarget = TripleUtils.getTarget(input, isForward);
            subAcc.begin(inputStateId, input, env, skipOutput);
            result = subAcc;

//            Node edgeInputSource = TripleUtils.getSource(input, true);
//
//            // endCurrentTarget(context);
//
//            if (Objects.equals(currentSourceNode, edgeInputSource)) {
//                ++seenTargetCount;
//                // System.err.println("items so far seen at path " + getPath() + ": " + seenTargetCount);
//
//                // If there is too many items we need still consume all the edges as usual
//                // but we call begin() on the accumulators with serialization disabled
////                boolean isTooMany = isSingle && seenTargetCount > 1;
////                if (isTooMany) {
////                    this.skipOutputStartedHere = true;
////                    AccJsonErrorHandler errorHandler = context.getErrorHandler();
////                    if (errorHandler != null) {
////                        PathJson path = getPath();
////                        errorHandler.accept(new AccJsonErrorEvent(path, "Multiple values encountered for a field that was declared to have at most a single one."));
////                    }
////                }
//
//                currentTarget = TripleUtils.getTarget(input, isForward);
//                targetAcc.begin(currentTarget, context, skipOutput || skipOutputStartedHere);
//                result = targetAcc;
//            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "AccStateCondition(matches: " + matchFieldId + ", target: " + subAcc + ")";
    }

    @Override
    public Iterator<? extends AccStateGon<I, E, K, V>> children() {
        return List.of(subAcc).iterator();
    }
}
