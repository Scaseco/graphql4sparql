package org.aksw.graphql4sparql.engine.rewrite;

/**
 * Adds <pre>@pretty</pre> to a query operation if any of its direct children has that directive.
 * If the directive is present on the query then the json output will be pretty printed.
 */
public class TransformPullPretty
    extends TransformDirectiveOnTopLevelFieldToQueryBase
{
    public TransformPullPretty() {
        super("pretty");
    }
}
