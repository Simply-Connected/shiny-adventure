package org.simply.connected.application.optimization.methods.newton.expression;

public class Div extends BinaryExpression {
    public Div(Expression left, Expression right) {
        super(left, right,"/" , (a, b) -> a / b);
    }

    @Override
    public Expression der(int varInd) {
        return new Div(
                new Add(
                        new Mul(left.der(varInd), right),
                        new Mul(left, right.der(varInd))
                ),
                new Mul(right, right)
        );
    }
}
