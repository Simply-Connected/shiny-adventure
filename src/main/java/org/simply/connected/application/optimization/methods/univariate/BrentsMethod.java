package org.simply.connected.application.optimization.methods.univariate;

import org.simply.connected.application.optimization.methods.univariate.model.BrentsData;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class BrentsMethod extends AbstractOptimizationMethod {
    public BrentsMethod(UnaryOperator<Double> function, double eps) {
        super(function, eps);
    }

    private void addIteration(double left, double right, double min, double x1, double x2, boolean isParabolic) {
        iterations.add(new BrentsData(left, min, right, x1, x2, isParabolic));
    }

    @Override
    public double minimize(double a, double b) {
        iterations.clear();

        double x = midPoint(a, b); // current min
        double w = x; // current second min
        double v = x; // previous w

        double fX = function.apply(x);
        double fW = function.apply(w);
        double fV = function.apply(v);

        double d = b - a; // current segment width
        double e = d; // previous segment width

        double u = 0;
        while ( ((b - a) / 2) > eps) {
            double g = e;
            e = d;
            if (distinct(x, w, v) && distinct(fX, fW, fV)) {
                u = ParabolicMethod.getMinX(x, w, v, fX, fW, fV);
            }
            if (u >= a + eps && u <= b - eps && Math.abs(u - x) < g / 2) {
                // x, w, v are unordered
                List<Double> sortedArgs = List.of(x, w, v).stream().sorted().collect(Collectors.toList());
                addIteration(sortedArgs.get(0), sortedArgs.get(2), u, u, u, true);
                d = Math.abs(u - x);
            } else {
                if (x < (b - a) / 2 ) {
                    u = x + GoldenRatioMethod.GOLDEN_RATIO * (b - x);
                    d = b - x;
                } else {
                    u = x - GoldenRatioMethod.GOLDEN_RATIO * (x - a);
                    d = x - a;
                }
                addIteration(a, b, x, Math.min(u, x), Math.max(u, x), false);
            }
            if (Math.abs(u - x) < eps) {
                u = x + Math.signum(u - x) * eps;
            }
            double fU = function.apply(u);

            if (fU <= fX) {
                if (u >= x) {
                    if (equalsDouble(a, x)) break;
                    a = x;
                } else {
                    if (equalsDouble(b, x)) break;
                    b = x;
                }
                v = w;
                w = x;
                x = u;
                fV = fW;
                fW = fX;
                fX = fU;
            } else {
                if (u >= x) {
                    if (equalsDouble(b, u)) break;
                    b = u;
                } else {
                    if (equalsDouble(a, u)) break;
                    a = u;
                }
                if (fU <= fW || equalsDouble(w, x)) {
                    v = w;
                    w = u;
                    fV = fW;
                    fW = fU;
                } else if (fU <= fV || equalsDouble(v, x) || equalsDouble(v, w)) {
                    v = u;
                    fV = fU;
                }
            }
        }
        return x;
    }

    private boolean distinct(double a, double b, double c) {
        return !equalsDouble(a, b) && !equalsDouble(b, c) && !equalsDouble(a, c);
    }

    private boolean equalsDouble(double a, double b) {
        return Math.abs(a - b) <= eps;
    }


}
