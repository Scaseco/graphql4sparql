package org.aksw.graphql4sparql.engine.io;

import java.util.Objects;

/**
     * Combines a writer for the generic types K, V with an in memory sink for an object notation Java type T (e.g. JsonElement).
     * This classes exposes this setup as an {@link ObjectNotationWriterInMemory}.
     * @param <T> The object type
     * @param <K> The key type
     * @param <V> The value type
     */
    public class ObjectNotationWriterInMemoryWrapper<T, K, V>
    implements ObjectNotationWriterWrapper<K, V>, ObjectNotationWriterInMemory<T, K, V>
{
    protected ObjectNotationWriter<K, V> delegate;
    protected ObjectNotationWriterInMemory<T, ?, ?> inMemorySink;

    public ObjectNotationWriterInMemoryWrapper(ObjectNotationWriter<K, V> delegate, ObjectNotationWriterInMemory<T, ?, ?> inMemorySink) {
        super();
        this.delegate = Objects.requireNonNull(delegate);
        this.inMemorySink = Objects.requireNonNull(inMemorySink);
    }

    @Override
    public T getProduct() {
        return inMemorySink.getProduct();
    }

    @Override
    public void clear() {
        inMemorySink.clear();
    }

    @Override
    public ObjectNotationWriter<K, V> getDelegate() {
        return delegate;
    }
}
