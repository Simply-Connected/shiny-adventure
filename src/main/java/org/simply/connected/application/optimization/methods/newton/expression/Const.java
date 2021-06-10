package org.simply.connected.application.optimization.methods.newton.expression;

public class Const implements Expression {
    public static final Const ZERO = new Const(0d);
    public static final Const ONE = new Const(1d);
    public static final Const TWO = new Const(2d);
    public static final Const THREE = new Const(3d);
    public static final Const FOUR = new Const(4d);
    public static final Const FIVE = new Const(5d);

    private final double val;

    public Const(double val) {
        this.val = val;
    }

    public static Const of(double val) {
        return new Const(val);
    }

    @Override
    public double eval(double... vars) {
        return val;
    }

    @Override
    public Expression der(int varInd) {
        return ZERO;
    }

    @Override
    public String toString() {
        return Double.toString(val);
    }
}
