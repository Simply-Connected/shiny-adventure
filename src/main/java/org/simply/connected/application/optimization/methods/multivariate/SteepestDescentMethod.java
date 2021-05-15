package org.simply.connected.application.optimization.methods.multivariate;

import org.simply.connected.application.optimization.methods.multivariate.math.QuadraticFunction;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.univariate.BrentsMethod;
import org.simply.connected.application.optimization.methods.univariate.OptimizationMethod;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class SteepestDescentMethod extends AbstractMultivariateOptimizationMethod {
    protected static final int MAX_STEP = 1000;

    public SteepestDescentMethod(QuadraticFunction function, double eps) {
        super(function, eps);
    }

    public SteepestDescentMethod(QuadraticFunction function,
                                    double eps,
                                    BiFunction<UnaryOperator<Double>, Double, OptimizationMethod> methodFactory) {
        super(function, eps, methodFactory);
    }

    @Override
    public Vector minimize(final Vector initialPoint) {
        iterationData.clear();

        Vector x = initialPoint;
        double y = function.apply(x);

        Function<Vector, Vector> gradient = getGradient();
        Vector Gx = gradient.apply(x);
        Vector p = normalize(negate(Gx));

        for (int it = 0; Math.abs(getLastY() - y) >= EPS && it < MAX_ITERATIONS; it++) {
            double curAlpha = getStep(x, p);
            addIteration(x, y);

            x = sum(x, product(curAlpha, p));
            y = function.apply(x);
            Gx = gradient.apply(x);
            p = normalize(negate(Gx));
        }

        addIteration(x, y);
        return x;
    }

    private double getStep(Vector x, Vector p) {
        UnaryOperator<Double> univariateFun = alpha -> function.apply(sum(x, product(alpha, p)));
        OptimizationMethod method;
        if (methodFactory == null) {
            method = new BrentsMethod(univariateFun, EPS);
        } else  {
            method = methodFactory.apply(univariateFun, EPS);
        }

        double optimalStep = method.minimize(0, MAX_STEP);
        unaryMethodIterations += method.getIterationData().size();
        return optimalStep;
    }
}
