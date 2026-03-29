package org.aksw.graphql4sparql.engine.api2;

import java.util.Arrays;
import java.util.List;

import org.apache.jena.sparql.core.Var;

/**
 * Builder interface for setting parent variables.
 *
 * @param <T> The builder type
 */
public interface HasParentVarBuilder<T extends HasParentVarBuilder<T>>
    extends HasSelf<T>
{
    /**
     * Sets parent variable names.
     *
     * @param varNames The variable names
     * @return This builder
     */
    default T parentVarNames(String...varNames) {
        List<String> list = Arrays.asList(varNames);
        parentVarNames(list);
        return self();
    }

    /**
     * Sets parent variable names.
     *
     * @param varNames The variable names
     * @return This builder
     */
    default  T parentVarNames(List<String> varNames) {
        List<Var> list = varNames == null ? null : Var.varList(varNames);
        parentVars(list);
        return self();
    }

    /**
     * Sets parent variables.
     *
     * @param vars The variables
     * @return This builder
     */
    default  T parentVars(Var ... vars) {
        List<Var> list = Arrays.asList(vars);
        parentVars(list);
        return self();
    }

    /**
     * Sets parent variables.
     *
     * @param vars The variables
     * @return This builder
     */
    default T parentVars(List<Var> vars) {
        setParentVars(vars);
        return self();
    }

    /**
     * Sets the parent variables.
     *
     * @param vars The variables to set
     */
    void setParentVars(List<Var> vars);
}
