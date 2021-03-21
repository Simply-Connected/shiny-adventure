package org.simply.connected.application.optimization.methods;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class FibonacciMethod extends AbstractOptimizationMethod {
    private List<Integer> fibonacci;

    public FibonacciMethod(UnaryOperator<Double> function, double eps) {
        super(function, eps);
    }

    @Override
    public double minimize(double a, double b) {
        iterations.clear();

        addIteration(a, b, midPoint(a, b));

        int n = calculateFibonacci(a, b, eps);

        double x1 = a + (b - a) / fibonacci.get(n) * fibonacci.get(n - 2);
        double x2 = a + b - x1;

        double y1 = function.apply(x1);
        double y2 = function.apply(x2);

        while (n-- > 0) {
            if (y1 < y2) {
                b = x2;
                x2 = x1;
                y2 = y1;
                x1 = a + b - x2;
                y1 = function.apply(x1);
            } else {
                a = x1;
                x1 = x2;
                y1 = y2;
                x2 = a + b - x1;
                y2 = function.apply(x2);
            }

            addIteration(a, b, midPoint(a, b));
        }

        return midPoint(a, b);
    }

    private int calculateFibonacci(double a, double b, Double eps) {
        fibonacci = new ArrayList<>(List.of(1, 1, 2));
        int i = 2;
        while (fibonacci.get(i) <= (b - a) / eps) {
            fibonacci.add(fibonacci.get(i) + fibonacci.get(i - 1));
            i++;
        }
        return i;
    }

}
