package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/** Builder base class that match on a state id and have a single sub-builder. */
public abstract class AggStateBuilderTransitionBase<I, E, K, V>
    extends AggStateBuilderTransitionMatch<I, E, K, V>
{
    protected AggStateBuilder<I, E, K, V> targetNodeMapper;

    public AggStateBuilderTransitionBase(Object matchStateId) {
        super(matchStateId);
    }

    public AggStateBuilder<I, E, K, V> getTargetNodeMapper() {
        return targetNodeMapper;
    }

    protected void validateTargetBuilder(AggStateBuilder<I, E, K, V> targetBuilder) {
        GonType thisType = this.getGonType();
        GonType childType = targetBuilder.getGonType();
        if (!childType.isValidChildOf(thisType)) {
            throw new RuntimeException("Incompatible types: parent: " + thisType + ", child: " + childType);
        }
    }

    public void setTargetBuilder(AggStateBuilder<I, E, K, V> targetNodeMapper) {
        validateTargetBuilder(targetNodeMapper);
        this.targetNodeMapper = targetNodeMapper;
    }
}
