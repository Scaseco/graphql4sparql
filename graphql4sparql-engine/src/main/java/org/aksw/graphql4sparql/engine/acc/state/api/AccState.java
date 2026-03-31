package org.aksw.graphql4sparql.engine.acc.state.api;

import java.io.IOException;
import java.util.Iterator;

/**
 * Interface for accumulator states in JSON accumulation operations.
 *
 * @param <I> The input type
 * @param <E> The environment type
 */
public interface AccState<I, E> {
    /**
     * Gets the parent state.
     *
     * @return The parent state, or null if this is the root
     */
    AccState<I, E> getParent();

    /**
     * Gets the root state of the accumulator hierarchy.
     *
     * @return The root state
     */
    default AccState<I, E> getRoot() {
        AccState<I, E> parent = getParent();
        return parent == null ? this : parent.getRoot();
    }

    // AccState<I, E> getValueParent();

    /**
     * Gets the id of the state for which this accumulator is notified.
     *
     * @return The state id
     */
    Object getStateId();

    /**
     * FIXME What exactly is id? Is it the value by which this acc connects to the parent?
     * The context and skipOutput flag are stored on the accumulator until end is called.
     *
     * @param stateId The state id
     * @param parentInput The parent input
     * @param env The environment
     * @param skipOutput Whether to skip output
     * @throws IOException If an I/O error occurs
     */
    void begin(Object stateId, I parentInput, E env, boolean skipOutput) throws IOException;

    /**
     * Checks if the state has begun processing.
     *
     * @return true if the state has begun, false otherwise
     */
    boolean hasBegun();

    /**
     * Transitions to a new state based on the input.
     *
     * @param inputStateId The stateId, typically extracted from the input by the driver. (Rationale: Only the driver needs to know how to extract the state from an input).
     * @param binding The input binding
     * @param env The environment
     * @return The new state after transition
     * @throws IOException If an I/O error occurs
     */
    AccState<I, E> transition(Object inputStateId, I binding, E env) throws IOException;

    /**
     * Ends the current state processing.
     *
     * @throws IOException If an I/O error occurs
     */
    void end() throws IOException;

    /**
     * Gets an iterator over sub-accumulators.
     * Sub-accumulators must be enumerated in the correct order.
     *
     * @return An iterator over child states
     */
    Iterator<? extends AccState<I, E>> children();
}
