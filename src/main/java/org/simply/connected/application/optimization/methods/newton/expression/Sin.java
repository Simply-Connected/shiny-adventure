package org.simply.connected.application.optimization.methods.newton.expression;

public class Sin extends UnaryExpression {
    public Sin(Expression expression) {
        super(expression);
    }

    @Override
    public double eval(double... vars) {
        return Math.sin(expression.eval(vars));
    }

    @Override
    public Expression der(int varInd) {
        return new Mul(expression.der(varInd), new Cos(expression));
    }

    @Override
    public String toPythonString() {
        return String.format("np.sin(%s)", expression.toPythonString());
    }

    @Override
    public String toString() {
        return String.format("sin(%s)", expression.toString());
    }
}
