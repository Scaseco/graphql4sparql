package org.aksw.graphql4sparql.engine.util;

import org.apache.jena.query.ARQ;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.util.Context;

public class ExecutionContextUtils {
    /** Creates an execution context primarily for use as a FunctionEnv */
    public static ExecutionContext createFunctionEnv() {
        Context context = ARQ.getContext().copy();
        Context.setCurrentDateTime(context);
        ExecutionContext result = ExecutionContext.create(context);
        return result;
    }
}
