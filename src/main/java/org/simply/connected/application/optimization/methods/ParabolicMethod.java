package org.simply.connected.application.optimization.methods;

import java.util.function.UnaryOperator;

public class ParabolicMethod extends AbstractOptimizationMethod {
    public ParabolicMethod(UnaryOperator<Double> function, double eps) {
        super(function, eps);
    }


    @Override
    public double minimize(double a, double b) {
        iterations.clear();

        double x1 = a;
        double x2 = midPoint(a, b);
        double x3 = b;

        double y1 = function.apply(x1);
        double y2 = function.apply(x2);
        double y3 = function.apply(x3);

        double minX = getMinX(x1, x2, x3, y1, y2, y3);
        double minY = function.apply(minX);
        addIteration(x1, x3, minX);

        double prevMinX;
        do {
            if (less(x1, minX, x2, x3) && minY >= y2) {
                x1 = minX;
                y1 = minY;
            }
            if (less(x1, minX, x2, x3) && minY < y2) {
                x3 = x2;
                y3 = y2;
                x2 = minX;
                y2 = minY;
            }
            if (less(x1, x2, minX, x3) && y2 >= minY) {
                x1 = x2;
                y1 = y2;
                x2 = minX;
                y2 = minY;
            }
            if (less(x1, x2, minX, x3) && y2 < minY) {
                x3 = minX;
                y3 = minY;
            }
            prevMinX = minX;
            minX = getMinX(x1, x2, x3, y1, y2, y3);
            minY = function.apply(minX);

            addIteration(x1, x3, minX);
        } while(Math.abs(minX - prevMinX) > eps);

        return minX;
    }

    public static UnaryOperator<Double> getParabolaWithin(double x1, double x2, double x3,
                                                          UnaryOperator<Double> function) {
        double y1 = function.apply(x1);
        double y2 = function.apply(x2);
        double y3 = function.apply(x3);

        return (x) ->
                    y1 +
                    getA1(x1, x2, y1, y2) * (x - x1) +
                    getA2(x1, x2, x3, y1, y2, y3) * (x - x1) * (x - x2);
    }

    public static double getMinX(double x1, double x2, double x3, double y1, double y2, double y3) {
        return (x1 + x2 - getA1(x1, x2, y1, y2) / getA2(x1, x2, x3, y1, y2, y3)) / 2;
    }


    private static double getA1(double x1, double x2, double y1, double y2) {
        return (y2 - y1) / (x2 - x1);
    }

    private static double getA2(double x1, double x2, double x3, double y1, double y2, double y3) {
        return (getA1(x1, x3, y1, y3) - getA1(x1, x2, y1, y2)) / (x3 - x2);
    }
    private boolean less(double x1, double x2, double x3, double x4) {
        return x1 < x2 && x2 < x3 && x3 < x4;
    }

}
