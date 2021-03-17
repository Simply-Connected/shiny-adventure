package org.simply.connected.application.optimization.methods;

import org.simply.connected.application.model.Segment;

import java.util.function.Function;

public class GoldenRatioMethod extends AbstractSteppedOptimizationMethod {
    private static final double GOLDEN_RATIO = (Math.sqrt(5) - 1) / 2;
    private static final double REVERSED_GOLDEN_RATIO = 1 - GOLDEN_RATIO;

    private double x1;
    private double x2;
    private double y1;
    private double y2;


    public GoldenRatioMethod(Function<Double, Double> function, Double eps, Segment segment) {
        super(function, eps, segment);

        double a = currSegment.getFrom();
        double b = currSegment.getTo();

        x1 = a + (b - a) * REVERSED_GOLDEN_RATIO;
        x2 = a + (b - a) * GOLDEN_RATIO;

        y1 = function.apply(x1);
        y2 = function.apply(x2);
    }

    @Override
    public boolean minimize() {
        double a = currSegment.getFrom();
        double b = currSegment.getTo();

        if ( (b - a) / 2  <= eps)  {
            return false;
        }

        if (y1 - y2 < eps) {
            currSegment = new Segment(a, x2);
            x2 = x1;
            y2 = y1;
            x1 = b - (b - a) * GOLDEN_RATIO;  // TODO НЕ РАБОТАЕТ ЕП ТВАЮ МАТ
            y1 = function.apply(x1);
        } else {
            currSegment = new Segment(x1, b);
            x1 = x2;
            y1 = y2;
            x2 = b - (b - a) * GOLDEN_RATIO;
            y2 = function.apply(x2);
        }
        currSegment = new Segment(x1, x2);
        return true;
    }
}
