package org.simply.connected.application.optimization.methods.multivariate.math;

import java.util.function.Function;

public class QuadraticFunction implements Function<Vector, Double> {
    private final Matrix a;
    private final Vector b;
    private final double c;

    private final double minVal, maxVal;
    public QuadraticFunction(Matrix a, Vector b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        minVal = a.getMin();
        maxVal = a.getMax();
    }

    @Override
    public Double apply(Vector x) {
        return 0.5 * Math.dotProduct(Math.product(a, x), x)  + Math.dotProduct(b, x) + c;
    }

    public Matrix getA() {
        return a;
    }

    public Vector getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getMinEigenValue() {
        return minVal;
    }

    public double getMaxEigenValue() {
        return maxVal;
    }
}
