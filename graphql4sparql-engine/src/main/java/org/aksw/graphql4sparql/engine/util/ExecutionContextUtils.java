package org.aksw.graphql4sparql.engine.util;

import org.apache.jena.query.ARQ;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.util.Context;

/**
 * Utility class for working with execution contexts.
 */
public class ExecutionContextUtils {
    private ExecutionContextUtils() {}

    /**
     * Creates an execution context primarily for use as a FunctionEnv.
     *
     * @return The execution context
     */
    public static ExecutionContext createFunctionEnv() {
        Context context = ARQ.getContext().copy();
        Context.setCurrentDateTime(context);
        ExecutionContext result = ExecutionContext.create(context);
        return result;
    }
}
