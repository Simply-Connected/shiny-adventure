package org.simply.connected.application.optimization.methods.newton.expression;

public class X implements Expression {
    private final int ind;

    public X(int ind) {
        this.ind = ind;
    }

    @Override
    public double eval(double... vars) {
        return vars[ind];
    }

    @Override
    public Expression der(int varInd) {
        return varInd == ind ? Const.ONE : Const.ZERO;
    }

    @Override
    public String toString() {
        return String.format("x%d", ind + 1);
    }
}
