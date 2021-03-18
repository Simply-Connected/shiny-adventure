package org.simply.connected.application.optimization.methods;

import org.simply.connected.application.model.Segment;

import java.util.function.UnaryOperator;

public class DichotomyMethod extends AbstractOptimizationMethod {
    public static final double DELTA = 1e-8;
    public DichotomyMethod(UnaryOperator<Double> function, double eps) {
        super(function, eps);
    }

    @Override
    public double minimize(double a, double b) {
        iterations.clear();

        while ((b - a) / 2 > eps) {
            double x1 = midPoint(a, b) - DELTA / 2;
            double x2 = midPoint(a, b) + DELTA / 2;

            double y1 = function.apply(x1);
            double y2 = function.apply(x2);


            if (y1 < y2) {
                b = x2;
            } else {
                a = x1;
            }

            addIteration(a, b, midPoint(a, b));
        }

        return midPoint(a, b);
    }


}
