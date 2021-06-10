package org.simply.connected.application.optimization.methods.newton.expression;

public class Sub extends BinaryExpression {
    protected Sub(Expression left, Expression right) {
        super(left, right, "-", (a, b) -> a - b);
    }

    @Override
    public Expression der(int varInd) {
        return new Sub(left.der(varInd), right.der(varInd));
    }
}
