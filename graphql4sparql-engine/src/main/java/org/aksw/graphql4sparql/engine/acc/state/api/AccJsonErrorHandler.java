package org.aksw.graphql4sparql.engine.acc.state.api;

import java.util.function.Consumer;

/**
 * Handler for JSON accumulation errors.
 * This interface extends Consumer to allow processing of error events.
 */
public interface AccJsonErrorHandler
    extends Consumer<AccJsonErrorEvent>
{
}
