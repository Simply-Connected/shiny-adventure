package org.simply.connected.application.optimization.methods;

import org.simply.connected.application.optimization.methods.model.BrentsData;
import org.simply.connected.application.optimization.methods.model.TernaryData;

import java.util.function.UnaryOperator;

public class GoldenRatioMethod extends AbstractOptimizationMethod {
    public static final double GOLDEN_RATIO = (Math.sqrt(5) - 1) / 2;

    public GoldenRatioMethod(UnaryOperator<Double> function, double eps) {
        super(function, eps);
    }

    private void addIteration(double left, double right, double min, double x1, double x2) {
        iterations.add(new TernaryData(left, min, right, x1, x2));
    }

    @Override
    public double minimize(double a, double b) {
        iterations.clear();


        double x1 = b - GOLDEN_RATIO * (b - a);
        double x2 = a + GOLDEN_RATIO * (b - a);
        double y1 = function.apply(x1);
        double y2 = function.apply(x2);

        addIteration(a, b, midPoint(a, b), x1, x2);

        while ((b - a) / 2 > eps) {
            if (y1 < y2) {
                b = x2;

                x2 = x1;
                y2 = y1;

                x1 = b - (b - a) * GOLDEN_RATIO;
                y1 = function.apply(x1);
            } else {
                a = x1;

                x1 = x2;
                y1 = y2;

                x2 = a + (b - a) * GOLDEN_RATIO;
                y2 = function.apply(x2);
            }

            addIteration(a, b, midPoint(a, b), x1, x2);
        }

        return midPoint(a, b);
    }
}
