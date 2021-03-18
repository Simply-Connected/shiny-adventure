package org.simply.connected.application.optimization.methods.stepped;

import org.simply.connected.application.model.Segment;

import java.util.function.Function;

public class DichotomyMethod extends AbstractSteppedOptimizationMethod {
    private final Double delta;

    public DichotomyMethod(Function<Double, Double> function, Double eps, Segment segment, Double delta) {
        super(function, eps, segment);
        this.delta = delta;
    }

    @Override
    public boolean minimize() {
        double a = currSegment.getFrom();
        double b = currSegment.getTo();

        if ( (b - a) / 2  <= eps)  {
            return false;
        }

        double x1 = (a + b - delta) / 2;
        double x2 = (a + b + delta) / 2;

        double y1 = function.apply(x1);
        double y2 = function.apply(x2);


        if (y1 < y2) {
            currSegment = new Segment(a, x2);
        } else {
            currSegment = new Segment(x1, b);
        }

        return true;
    }
}
