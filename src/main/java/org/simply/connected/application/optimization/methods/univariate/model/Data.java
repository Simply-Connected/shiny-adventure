package org.simply.connected.application.optimization.methods.univariate.model;


/**
 * Information data class for iteration of Optimization algorithm
 * left <= min <= right
 * min - function's min x coordinate on current iteration
 * left, right - borders of min
 */
public class Data {
    private final double left;
    private final double right;
    private final double min;


    public Data(double a, double b, double c) {
        left = a;
        min = b;
        right = c;
    }


    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public double getMin() {
        return min;
    }

    @Override
    public String toString() {
        return String.format("[%.10f ; %.10f] min: < %.10f >", left, right, min);
    }
}
