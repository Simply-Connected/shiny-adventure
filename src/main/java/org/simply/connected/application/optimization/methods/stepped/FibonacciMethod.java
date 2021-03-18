package org.simply.connected.application.optimization.methods.stepped;

import org.simply.connected.application.model.Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FibonacciMethod extends AbstractSteppedOptimizationMethod {

    private int n;

    private final List<Integer> fibonacci = new ArrayList<>(List.of(1, 1, 2));


    private double x1;
    private double x2;
    private double y1;
    private double y2;

    public FibonacciMethod(Function<Double, Double> function, Double eps, Segment segment) {
        super(function, eps, segment);
        this.n = calculateFibonacci(segment, eps);

        double a = currSegment.getFrom();
        double b = currSegment.getTo();

        x1 = a + (b - a) / fibonacci.get(n) * fibonacci.get(n - 2);
        x2 = a + b - x1;

        y1 = function.apply(x1);
        y2 = function.apply(x2);

    }

    private int calculateFibonacci(Segment segment, Double eps) {
        int i = 2;
        while (fibonacci.get(i) <= (segment.getTo() - segment.getFrom()) / eps) {
            fibonacci.add(fibonacci.get(i) + fibonacci.get(i - 1));
            i++;
        }
        return i;
    }

    @Override
    public boolean minimize() {
        double a = currSegment.getFrom();
        double b = currSegment.getTo();

        if (n == 1) {
            return false;
        }
        n--;

        if (y1 < y2) {
            currSegment = new Segment(a, x2);
            x2 = x1;
            y2 = y1;
            x1 = a + currSegment.getTo() - x2;
            y1 = function.apply(x1);
        } else {
            currSegment = new Segment(x1, b);
            x1 = x2;
            y1 = y2;
            x2 = currSegment.getFrom() + b - x1;
            y2 = function.apply(x2);
        }
        return true;
    }
}
