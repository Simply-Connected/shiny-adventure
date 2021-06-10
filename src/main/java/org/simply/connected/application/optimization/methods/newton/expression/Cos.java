package org.simply.connected.application.optimization.methods.newton.expression;

public class Cos extends UnaryExpression {
    public Cos(Expression expression) {
        super(expression);
    }

    @Override
    public double eval(double... vars) {
        return Math.cos(expression.eval(vars));
    }

    @Override
    public Expression der(int varInd) {
        return new Mul(expression.der(varInd), new Mul(Const.of(-1), new Sin(expression)));
    }

    @Override
    public String toPythonString() {
        return String.format("np.cos(%s)", expression.toPythonString());
    }

    @Override
    public String toString() {
        return String.format("cos(%s)", expression.toString());
    }
}
