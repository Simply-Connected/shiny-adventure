package org.simply.connected.application.optimization.methods.univariate.model;

/**
 * Data for ternary segment division methods
 * x1, x2 - points of division of segments in the iteration
 */
public class TernaryData extends Data {
    private final double x1;
    private final double x2;

    public TernaryData(double a, double min, double c, double x1, double x2) {
        super(a, min, c);
        this.x1 = x1;
        this.x2 = x2;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }
}
