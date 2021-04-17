package org.simply.connected.application.optimization.methods.multivariate;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.univariate.OptimizationMethod;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class SteepestDescentMethod extends AbstractMultivariateOptimizationMethod {

    public SteepestDescentMethod(Function<Vector, Double> function, double eps) {
        super(function, eps);
    }

    public SteepestDescentMethod(Function<Vector, Double> function,
                                    double eps,
                                    BiFunction<UnaryOperator<Double>, Double, OptimizationMethod> methodFactory) {
        super(function, eps, methodFactory);
    }

    @Override
    public Vector minimize(final Vector initalPoint) {
        iterationData.clear();

        Vector x = initalPoint;

        Function<Vector, Vector> gradient = getGradient();
        Vector Gx = gradient.apply(x);
        Vector p = normalize(negate(Gx));

        for (int it = 0; norm(Gx) >= EPS && it < MAX_ITERATIONS; it++) {
            double curAlpha = getAlpha(x, p);
            addIteration(x, p, curAlpha);

            x = sum(x, product(curAlpha, p));
            Gx = gradient.apply(x);
            p = normalize(negate(Gx));
        }

        addIteration(x, p,  0);
        return x;
    }

}
