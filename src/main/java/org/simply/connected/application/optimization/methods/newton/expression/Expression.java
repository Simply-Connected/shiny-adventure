package org.simply.connected.application.optimization.methods.newton.expression;

public interface Expression {
    double eval(double... vars);

    Expression der(int varInd);

    default String toPythonString() {
        return toString();
    }
}
