package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;

import org.aksw.graphql4sparql.engine.acc.state.api.AccJsonErrorHandler;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;
import org.aksw.graphql4sparql.engine.gon.model.GonProvider;
import org.aksw.graphql4sparql.engine.io.ObjectNotationWriter;

/**
  * Accumulator for literal properties.
  *
  * @param <I> The input type
  * @param <E> The environment type
  * @param <K> The key type
  * @param <V> The value type
  */
public class AccStateLiteralProperty<I, E, K, V>
    extends AccStatePropertyBase<I, E, K, V>
{
    /** Whether to skip if the value is null. */
    protected boolean skipIfNull;
    /** The array mode for handling multiple values. */
    protected ArrayMode arrayMode;
    /** Function to extract the value from input. */
    protected BiFunction<I, E, ? extends V> inputToValue;

    /** Whether skip output has started here. */
    protected boolean skipOutputStartedHere = false;

    /** Count of targets seen since last call to begin(). */
    protected long seenTargetCount = 0;

    /** The value. */
    protected Object value;

    /**
     * Creates a new AccStateLiteralProperty.
     *
     * @param matchStateId The match state id
     * @param memberKey The member key
     * @param isSingle Whether this is a single value
     * @param skipIfNull Whether to skip if the value is null
     * @param arrayMode The array mode for handling multiple values
     * @param inputToValue Function to extract the value from input
     */
    public AccStateLiteralProperty(Object matchStateId, K memberKey, boolean isSingle, boolean skipIfNull, ArrayMode arrayMode, BiFunction<I, E, ? extends V> inputToValue) {
        super(matchStateId, memberKey, isSingle);
        this.skipIfNull = skipIfNull;
        this.inputToValue = inputToValue;
        this.arrayMode = Objects.requireNonNull(arrayMode);
    }

    @Override
    public GonType getGonType() {
        GonType result = switch (arrayMode) {
        case OFF -> GonType.ENTRY;
        case FLAT -> GonType.LITERAL;
        case NESTED -> GonType.ARRAY;
        default -> throw new IllegalStateException("Should not happen");
        };
        return result;
    }

    /**
     * Creates a new AccStateLiteralProperty.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param matchStateId The match state id
     * @param memberKey The member key
     * @param isSingle Whether this is a single value
     * @param skipIfNull Whether to skip if the value is null
     * @param arrayMode The array mode for handling multiple values
     * @param inputToValue Function to extract the value from input
     * @return A new AccStateLiteralProperty instance
     */
    public static <I, E, K, V> AccStateLiteralProperty<I, E, K, V> of(Object matchStateId, K memberKey, boolean isSingle, boolean skipIfNull, ArrayMode arrayMode, BiFunction<I, E, ? extends V> inputToValue) {
        return new AccStateLiteralProperty<>(matchStateId, memberKey, isSingle, skipIfNull, arrayMode, inputToValue);
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
        skipOutputStartedHere = false;

        if (!skipOutput) {
            if (context.isMaterialize()) {
                GonProvider<K, V> gonProvider = context.getGonProvider();

                if (true) {
                    throw new RuntimeException("TODO update materialization");
                }

                value = isSingle
                        ? gonProvider.newNull()
                        : gonProvider.newArray();
            }

            if (context.isSerialize()) {
                ObjectNotationWriter<K, V> writer = context.getJsonWriter();
                if (!isSingle) {
                    if (ArrayMode.OFF.equals(arrayMode)) {
                        writer.name(memberKey);
                    }

                    if (!ArrayMode.FLAT.equals(arrayMode)) {
                        writer.beginArray();
                    }
                } else {
                    if (ArrayMode.NESTED.equals(arrayMode)) {
                        writer.beginArray();
                    }
                }
            }
        }
    }

    /**
     * Accepts an input tuple (usually binding) if source and field id match that of the current state.
     *
     * @param inputStateId The input state id
     * @param input The input
     * @param env The environment
     * @return The current state
     * @throws IOException If an I/O error occurs
     */
    @Override
    public AccStateGon<I, E, K, V> transitionActual(Object inputStateId, I input, E env) throws IOException {
        // AccStateTypeProduceNode<I, E, K, V> result = null;
        AccStateGon<I, E, K, V> result = null;
        // Object inputStateId = getInputStateId(input, env);
        if (Objects.equals(matchStateId, inputStateId)) {
            // if (Objects.equals(currentSourceNode, edgeInputSource)) {
                ++seenTargetCount;
                // System.err.println("items so far seen at path " + getPath() + ": " + seenTargetCount);

                // If there is too many items we need still consume all the edges as usual
                // but we call begin() on the accumulators with serialization disabled
                boolean isTooMany = isSingle && seenTargetCount > 1;
                if (isTooMany) {
                    this.skipOutputStartedHere = true;
                    AccJsonErrorHandler errorHandler = context.getErrorHandler();
                    if (errorHandler != null) {
                        throw new RuntimeException("Error handler not yet implemented");
                        // PathJson path = getPath();
                        // errorHandler.accept(new AccJsonErrorEvent(path, "Multiple values encountered for a field that was declared to have at most a single one."));
                    }
                } else {
                    V currentValue = inputToValue.apply(input, env);
                    if (!(currentValue == null && skipIfNull)) {
                        if (!skipOutput) {
                            if (context.isSerialize()) {
                                ObjectNotationWriter<K, V> writer = context.getJsonWriter();

                                // In array mode the member key is written on begin.
                                if (isSingle) {
                                    if (ArrayMode.OFF.equals(arrayMode)) {
                                        writer.name(memberKey);
                                    }
                                }
                                writer.value(currentValue);
                            }
                        }
                    }
                }
               //  currentTarget = isForward ? o : s; // TripleUtils.getTarget(input, isForward);
            // }
            result = this;
        }
        return result;
    }

    @Override
    public void endActual() throws IOException {
        if (!skipOutput) {
            if (context.isMaterialize()) {
                GonProvider<K, V> gonProvider = context.getGonProvider();
                // XXX Calling parent.getValue() here causes IllegalState because
                // getValue() must only be called after end()
                // So we access the field directly
                if (parent != null) {
                    // Turns null into JsonNull
                    Object elt = value == null ? gonProvider.newNull() : value;
                    // AccJsonObjectLikeBase acc = (AccJsonObjectLikeBase)parent;

                    // TODO Can we have the parent access the value in the child rather than the child accessing the parent?
                    // AccStateTypeProduceObject
                    AccStateGon<I, E, K, V> valueParent = getParent();
                    throw new RuntimeException("materialization no longer supported");
                    // Object parentValue = valueParent.getValue();
                    // parentAcc.value.getAsObject().getMembers().put(memberKey, elt);
                    // gonProvider.setProperty(parentValue, memberKey, elt);
                }
            }

            if (context.isSerialize()) {
                ObjectNotationWriter<K, V> writer = context.getJsonWriter();
                if (!isSingle) {
                    if (!ArrayMode.FLAT.equals(arrayMode)) {
                        writer.endArray();
                    }
                } else {
                    if (seenTargetCount == 0) {
                        if (!skipIfNull) {
                            if (ArrayMode.OFF.equals(arrayMode)) {
                                writer.name(memberKey);
                            }
                            writer.nullValue();
                        }
                    }

                    if (ArrayMode.NESTED.equals(arrayMode)) {
                        writer.endArray();
                    }
                }
            }
        }

        // seenTargetCount = 0;
    }

    @Override
    public String toString() {
        return "AccStateLiteralProperty(matches: " + matchStateId + ", currentInput: " + currentInput + ")";
    }


    @Override
    public Iterator<? extends AccStateGon<I, E, K, V>> children() {
        return Collections.emptyIterator();
    }
}
