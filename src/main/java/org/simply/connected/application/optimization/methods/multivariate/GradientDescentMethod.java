package org.simply.connected.application.optimization.methods.multivariate;

import org.simply.connected.application.optimization.methods.multivariate.math.QuadraticFunction;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.univariate.BrentsMethod;
import org.simply.connected.application.optimization.methods.univariate.OptimizationMethod;

import java.util.function.Function;
import java.util.function.UnaryOperator;

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
        double lastFx = Double.MAX_VALUE;

        double curAlpha = initialPoint.getArity() * START_STEP_MULTIPLIER;
        Function<Vector, Vector> gradient = getGradient();
        Vector p = normalize(negate(gradient.apply(x)));


        for (int it = 0; Math.abs(lastFx - Fx) >= EPS && it < MAX_ITERATIONS; it++) {
            addIteration(x, p,  curAlpha);
            Vector y = sum(x, product(curAlpha, p));
            double Fy = function.apply(y);
            while(Fy >= Fx) {
                curAlpha /= 2;
                y = sum(x, product(curAlpha, p));
                Fy = function.apply(y);
            }
            x = y;
            p = normalize(negate(gradient.apply(x)));
            lastFx = Fx;
            Fx = Fy;
        }
        addIteration(x, p, curAlpha);
        return x;
    }
}
