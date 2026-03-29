package org.aksw.graphql4sparql.engine.api2;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A fragment in a GraphQL query.
 */
public class Fragment
    extends SelectionSet
    implements ConnectiveNode
{

    /**
     * Creates a new Fragment.
     *
     * @param defaultTargetVars The default target variables
     * @param visibleVars The set of visible variables
     * @param selections The selections
     */
    public Fragment(List defaultTargetVars, Set visibleVars, Map selections) {
        super(defaultTargetVars, visibleVars, selections);
        // TODO Auto-generated constructor stub
    }

    @Override
    public <T> T accept(ConnectiveVisitor<T> visitor) {
        // TODO Auto-generated method stub
        return null;
    }

}
//    public Fragment(List<Var> defaultTargetVars, Set<Var> visibleVars, Map<String, Selection> selections) {
//        super(defaultTargetVars, visibleVars, selections);
//    }
//
//    @Override
//    public <T> T accept(ConnectiveVisitor<T> visitor) {
//        T result = visitor.visit(this);
//        return result;
//    }
//
//    public static FragmentBuilder newBuilder() {
//        return new FragmentBuilder();
//    }
//
//    @Override
//    public String toString() {
//        String result = ConnectiveVisitorToString.toString(this);
//        return result;
//    }
//}
