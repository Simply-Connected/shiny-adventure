package org.simply.connected.application.optimization.methods.newton.expression;

import java.util.function.DoubleBinaryOperator;

public abstract class BinaryExpression implements Expression {
    protected final Expression left;
    protected final Expression right;
    private final String operationSymbol;
    private final DoubleBinaryOperator operator;

    protected BinaryExpression(Expression left,
                               Expression right,
                               String operationSymbol,
                               DoubleBinaryOperator operator) {
        this.left = left;
        this.right = right;
        this.operationSymbol = operationSymbol;
        this.operator = operator;
    }

    @Override
    public double eval(double... vars) {
        return operator.applyAsDouble(left.eval(vars), right.eval(vars));
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, operationSymbol, right);
    }

    @Override
    public String toPythonString() {
        return String.format("(%s %s %s)", left.toPythonString(), operationSymbol, right.toPythonString());
    }
}
