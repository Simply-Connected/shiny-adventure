package org.simply.connected.application.optimization.methods.multivariate;

import org.simply.connected.application.optimization.methods.multivariate.math.QuadraticFunction;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;

import java.util.function.Function;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class GradientDescentMethod extends AbstractMultivariateOptimizationMethod {
    private static final double START_STEP_MULTIPLIER = 0.5;

    public GradientDescentMethod(QuadraticFunction function, double eps) {
        super(function, eps);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public Vector minimize(final Vector initialPoint) {
        iterationData.clear();

        Vector x = initialPoint;
        double Fx = function.apply(x);

        double curAlpha = initialPoint.getArity() * START_STEP_MULTIPLIER;
        Function<Vector, Vector> gradient = getGradient();
        Vector p = normalize(negate(gradient.apply(x)));


        for (int it = 0; Math.abs(getLastY() - Fx) >= EPS && it < MAX_ITERATIONS; it++) {
            addIteration(x, Fx);
            Vector y = sum(x, product(curAlpha, p));
            double Fy = function.apply(y);
            while(Fy >= Fx) {
                curAlpha /= 2;
                y = sum(x, product(curAlpha, p));
                Fy = function.apply(y);
            }
            x = y;
            p = normalize(negate(gradient.apply(x)));
            Fx = Fy;
        }
        addIteration(x, Fx);
        return x;
    }
}
