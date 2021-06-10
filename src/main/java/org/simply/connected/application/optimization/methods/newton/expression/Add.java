package org.simply.connected.application.optimization.methods.newton.expression;

public class Add extends BinaryExpression {
    public Add(Expression left, Expression right) {
        super(left, right, "+", Double::sum);
    }

    @Override
    public Expression der(int varInd) {
        return new Add(left.der(varInd), right.der(varInd));
    }
}
