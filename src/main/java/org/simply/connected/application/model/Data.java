package org.simply.connected.application.model;


import java.util.List;
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
        return "[" +
                String.format("%.5f", left) +
                " ; " +
                String.format("%.5f", min) +
                " ; " +
                String.format("%.5f", right) +
                "]";
    }
}
