package org.simply.connected.application.optimization.methods.newton.expression;

public class Mul extends BinaryExpression {
    public Mul(Expression left, Expression right) {
        super(left, right, "*", (a, b) -> a * b);
    }

    @Override
    public Expression der(int varInd) {
        return new Add(
                new Mul(left.der(varInd), right),
                new Mul(left, right.der(varInd))
        );
    }
}
