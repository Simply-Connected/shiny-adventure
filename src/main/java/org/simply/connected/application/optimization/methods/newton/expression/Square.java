package org.simply.connected.application.optimization.methods.newton.expression;

public class Square extends UnaryExpression {
    public Square(Expression expression) {
        super(expression);
    }

    @Override
    public double eval(double... vars) {
        double res = expression.eval(vars);
        return res * res;
    }

    @Override
    public Expression der(int varInd) {
        return new Mul(new Mul(Const.TWO, expression), expression.der(varInd));
    }

    @Override
    public String toPythonString() {
        return String.format("%s**2", expression.toPythonString());
    }

    @Override
    public String toString() {
        return String.format("%s^2", expression);
    }
}
