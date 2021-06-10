package org.simply.connected.application.optimization.methods.newton.expression;

public class Power extends UnaryExpression {
    private final int exp;

    public Power(Expression expression, int exp) {
        super(expression);
        this.exp = exp;
    }


    @Override
    public double eval(double... vars) {
        if (exp == 0) {
            return 1;
        }
        return Math.pow(expression.eval(vars), exp);
    }

    @Override
    public Expression der(int varInd) {
        if (exp == 0) {
            return Const.ZERO;
        }
        return new Mul(
                Const.of(exp),
                new Mul(expression.der(varInd), new Power(expression, exp - 1))
        );
    }

    @Override
    public String toPythonString() {
        return String.format("(%s)**%d", expression.toPythonString(), exp);
    }

    @Override
    public String toString() {
        return String.format("(%s)^%d", expression, exp);
    }
}
