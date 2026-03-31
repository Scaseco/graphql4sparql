package org.aksw.graphql4sparql.engine.api2;

import java.util.Arrays;
import java.util.List;

import org.apache.jena.sparql.core.Var;

/**
 * Builder interface for setting connect variables.
 *
 * @param <T> The builder type
 */
public interface HasConnectVarBuilder<T extends HasConnectVarBuilder<T>>
    extends HasSelf<T>
{
    /**
     * Sets connect variable names.
     *
     * @param varNames The variable names
     * @return This builder
     */
    default T connectVarNames(String...varNames) {
        List<String> list = Arrays.asList(varNames);
        connectVarNames(list);
        return self();
    }

    /**
     * Sets connect variable names.
     *
     * @param varNames The variable names
     * @return This builder
     */
    default  T connectVarNames(List<String> varNames) {
        List<Var> list = varNames == null ? null : Var.varList(varNames);
        connectVars(list);
        return self();
    }

    /**
     * Sets connect variables.
     *
     * @param vars The variables
     * @return This builder
     */
    default  T connectVars(Var ... vars) {
        List<Var> list = Arrays.asList(vars);
        connectVars(list);
        return self();
    }

    /**
     * Sets connect variables.
     *
     * @param vars The variables
     * @return This builder
     */
    default T connectVars(List<Var> vars) {
        setConnectVars(vars);
        return self();
    }

    /**
     * Sets the connect variables.
     *
     * @param vars The variables to set
     */
    void setConnectVars(List<Var> vars);
}
