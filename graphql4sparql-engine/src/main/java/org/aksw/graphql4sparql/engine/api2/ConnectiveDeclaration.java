package org.aksw.graphql4sparql.engine.api2;

import java.util.List;
import java.util.Set;

import org.apache.jena.sparql.core.Var;

/**
 * Describes the variables for a {@link Connective} but does not specify the graph pattern.
 */
public class ConnectiveDeclaration
    extends BasicConnectInfo
{
    /**
     * The variables of the given element which to join on the parent variables.
     */
    protected final List<Var> connectVars;

    /**
     * Creates a new ConnectiveDeclaration.
     *
     * @param defaultTargetVars The default target variables
     * @param visibleVars The set of visible variables
     * @param connectVars The connect variables
     */
    public ConnectiveDeclaration(List<Var> defaultTargetVars, Set<Var> visibleVars, List<Var> connectVars) {
        super(defaultTargetVars, visibleVars);
        this.connectVars = connectVars;
    }

    /**
     * Returns the connect variables.
     *
     * @return The connect variables
     */
    public List<Var> getConnectVars() {
        return connectVars;
    }
}
