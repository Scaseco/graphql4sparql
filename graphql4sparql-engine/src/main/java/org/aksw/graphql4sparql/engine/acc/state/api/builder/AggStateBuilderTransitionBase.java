package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Builder base class that match on a state id and have a single sub-builder.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class AggStateBuilderTransitionBase<I, E, K, V>
    extends AggStateBuilderTransitionMatch<I, E, K, V>
{
    /**
     * The target node mapper.
     */
    protected AggStateBuilder<I, E, K, V> targetNodeMapper;

    /**
     * Creates a new AggStateBuilderTransitionBase.
     *
     * @param matchStateId The match state ID
     */
    public AggStateBuilderTransitionBase(Object matchStateId) {
        super(matchStateId);
    }

    /**
     * Returns the target node mapper.
     *
     * @return The target node mapper
     */
    public AggStateBuilder<I, E, K, V> getTargetNodeMapper() {
        return targetNodeMapper;
    }

    /**
     * Validates the target builder.
     *
     * @param targetBuilder The target builder to validate
     * @throws RuntimeException If the target builder is invalid
     */
    protected void validateTargetBuilder(AggStateBuilder<I, E, K, V> targetBuilder) {
        GonType thisType = this.getGonType();
        GonType childType = targetBuilder.getGonType();
        if (!childType.isValidChildOf(thisType)) {
            throw new RuntimeException("Incompatible types: parent: " + thisType + ", child: " + childType);
        }
    }

    /**
     * Sets the target builder.
     *
     * @param targetNodeMapper The target builder to set
     * @throws RuntimeException If the target builder is invalid
     */
    public void setTargetBuilder(AggStateBuilder<I, E, K, V> targetNodeMapper) {
        validateTargetBuilder(targetNodeMapper);
        this.targetNodeMapper = targetNodeMapper;
    }
}
