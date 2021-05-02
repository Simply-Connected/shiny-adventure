package org.simply.connected.application.optimization.methods.multivariate;

import org.simply.connected.application.optimization.methods.multivariate.math.QuadraticFunction;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;

import java.util.function.Function;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class GradientDescentMethod extends AbstractMultivariateOptimizationMethod {

    public GradientDescentMethod(QuadraticFunction function, double eps) {
        super(function, eps);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public Vector minimize(final Vector initialPoint) {
        iterationData.clear();

        Vector x = initialPoint;
        double fX = function.apply(x);
        double curAlpha = 2d / (function.getMinEigenValue() + function.getMaxEigenValue());
        Function<Vector, Vector> gradient = getGradient();
        Vector p = negate(gradient.apply(x));

        addIteration(x, p,  curAlpha);

        for (int it = 0; norm(p) >= EPS && it < MAX_ITERATIONS; it++) {
            Vector y = sum(x, product(curAlpha / norm(p), p));
            double fY = function.apply(y);
            while(fY >= fX) {
                curAlpha /= 2;
                y = sum(x, product(curAlpha / norm(p), p));
                fY = function.apply(y);
             //   addIteration(x, p, curAlpha);
            }
            x = y;
            p = negate(gradient.apply(x));
            fX = fY;
            addIteration(x, p, curAlpha);
        }
        return x;
    }
}
