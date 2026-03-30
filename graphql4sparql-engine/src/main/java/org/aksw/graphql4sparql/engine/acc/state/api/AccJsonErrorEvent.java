package org.aksw.graphql4sparql.engine.acc.state.api;

/**
 * Event representing a JSON accumulation error.
 *
 * @param <K> The key type
 */
public class AccJsonErrorEvent<K> {
    /** Where the error occurred */
    protected PathGon<K> path;

    /** The error message. */
    protected String message;

    /**
     * Creates a new AccJsonErrorEvent.
     *
     * @param path The path where the error occurred
     * @param message The error message
     */
    public AccJsonErrorEvent(PathGon<K> path, String message) {
        super();
        this.path = path;
        this.message = message;
    }

    /**
     * Gets the path where the error occurred.
     *
     * @return The path
     */
    public PathGon<K> getPath() {
        return path;
    }

    /**
     * Gets the error message.
     *
     * @return The error message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Json accumulation error at path " + path + ": " + message;
    }
}
