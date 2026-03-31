package org.aksw.graphql4sparql.engine.rewrite;

/**
 * Adds <pre>@debug</pre> to a query operation if any of its direct children has that directive.
 */
public class TransformPullDebug
    extends TransformDirectiveOnTopLevelFieldToQueryBase
{
    public TransformPullDebug() {
        super("debug");
    }
}
