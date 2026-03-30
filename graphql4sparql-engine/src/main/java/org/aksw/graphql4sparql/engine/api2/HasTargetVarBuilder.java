package org.aksw.graphql4sparql.engine.api2;

import java.util.Arrays;
import java.util.List;

import org.apache.jena.sparql.core.Var;

/**
 * Builder interface for setting target variables.
 *
 * @param <T> The builder type
 */
public interface HasTargetVarBuilder<T extends HasTargetVarBuilder<T>>
    extends HasSelf<T>
{
    /**
     * Sets target variable names.
     *
     * @param varNames The variable names
     * @return This builder
     */
    default T targetVarNames(String...varNames) {
        List<String> list = Arrays.asList(varNames);
        targetVarNames(list);
        return self();
    }

    /**
     * Sets target variable names.
     *
     * @param varNames The variable names
     * @return This builder
     */
    default  T targetVarNames(List<String> varNames) {
        List<Var> list = varNames == null ? null : Var.varList(varNames);
        targetVars(list);
        return self();
    }

    /**
     * Sets target variables.
     *
     * @param vars The variables
     * @return This builder
     */
    default  T targetVars(Var ... vars) {
        List<Var> list = Arrays.asList(vars);
        targetVars(list);
        return self();
    }

    /**
     * Sets target variables.
     *
     * @param vars The variables
     * @return This builder
     */
    default T targetVars(List<Var> vars) {
        setTargetVars(vars);
        return self();
    }

    /**
     * Sets the target variables.
     *
     * @param vars The variables to set
     */
    void setTargetVars(List<Var> vars);
}
