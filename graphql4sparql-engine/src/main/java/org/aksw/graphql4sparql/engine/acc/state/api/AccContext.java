package org.aksw.graphql4sparql.engine.acc.state.api;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import org.aksw.graphql4sparql.engine.gon.model.GonProvider;
import org.aksw.graphql4sparql.engine.io.ObjectNotationWriter;
import org.aksw.graphql4sparql.engine.io.RdfObjectNotationWriter;
import org.aksw.graphql4sparql.engine.io.RdfObjectNotationWriterViaJson;

/**
 * Context for JSON accumulation operations.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public class AccContext<K, V> {
    /** Whether to accumulate a JsonElement */
    protected boolean materialize;

    /** Whether to stream to the jsonWriter */
    protected boolean serialize;

    /** The writer for generalized object notation. May support streaming. */
    protected ObjectNotationWriter<K, V> writer;

    /** The provider for building in memory objects. */
    protected GonProvider<K, V> gonProvider;

    /** The error handler for JSON accumulation errors. */
    protected AccJsonErrorHandler errorHandler = null;

    /**
     * Creates a new AccContext.
     *
     * @param writer The writer for generalized object notation
     * @param materialize Whether to accumulate a JsonElement
     * @param serialize Whether to stream to the jsonWriter
     */
    public AccContext(ObjectNotationWriter<K, V> writer, boolean materialize, boolean serialize) {
        super();
        this.writer = writer;
        this.materialize = materialize;
        this.serialize = serialize;
    }

    /**
     * Create a context that only materializes.
     *
     * @return The materializing context
     */
    public static AccContextRdf materializing() {
        return new AccContextRdf(null, true, false);
    }

    /**
     * Create a context that serializes using a Gson instance and JsonWriter.
     *
     * @param gson The Gson instance for JSON serialization
     * @param jsonWriter The JsonWriter for streaming output
     * @return The serializing context
     */
    public static AccContextRdf serializing(Gson gson, JsonWriter jsonWriter) {
        RdfObjectNotationWriter writer = new RdfObjectNotationWriterViaJson(gson, jsonWriter);
        return serializing(writer);
    }

    /**
     * Create a context that serializes using an RdfObjectNotationWriter.
     *
     * @param writer The RdfObjectNotationWriter for streaming output
     * @return The serializing context
     */
    public static AccContextRdf serializing(RdfObjectNotationWriter writer) {
        return new AccContextRdf(writer, false, true);
    }

    /**
     * Create a context that serializes using an ObjectNotationWriter.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param writer The ObjectNotationWriter for streaming output
     * @return The serializing context
     */
    public static <K, V> AccContext<K, V> serializing(ObjectNotationWriter<K, V> writer) {
        return new AccContext<>(writer, false, true);
    }

    /**
     * Sets the writer for generalized object notation.
     *
     * @param writer The writer to set
     */
    public void setWriter(ObjectNotationWriter<K, V> writer) {
        this.writer = writer;
    }

    /**
     * Sets the error handler for JSON accumulation errors.
     *
     * @param errorHandler The error handler to set
     */
    public void setErrorHandler(AccJsonErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * Gets the error handler for JSON accumulation errors.
     *
     * @return The error handler
     */
    public AccJsonErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Checks if materialization is enabled.
     *
     * @return true if materialization is enabled, false otherwise
     */
    public boolean isMaterialize() {
        return materialize;
    }

    /**
     * Checks if serialization is enabled.
     *
     * @return true if serialization is enabled, false otherwise
     */
    public boolean isSerialize() {
        return serialize;
    }

    /**
     * Gets the writer for generalized object notation.
     *
     * @return The writer
     */
    public ObjectNotationWriter<K, V> getJsonWriter() {
        return writer;
    }

    /**
     * Gets the provider for building in memory objects.
     *
     * @return The GonProvider
     */
    public GonProvider<K, V> getGonProvider() {
        return gonProvider;
    }

    /**
     * Sets the provider for building in memory objects.
     *
     * @param gonProvider The GonProvider to set
     */
    public void setGonProvider(GonProvider<K, V> gonProvider) {
        this.gonProvider = gonProvider;
    }

//public Gson getGson() {
//return gson;
//}

//public JsonWriter getJsonWriter() {
//return jsonWriter;
//}
}
