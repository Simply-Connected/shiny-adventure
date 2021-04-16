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
        Vector prevX = new Vector(x.getArity(), 0); // TODO

        Function<Vector, Vector> gradient = getGradient();
        Vector p = normalize(negate(gradient.apply(x)));

        for (int it = 0; distance(prevX, x) >= EPS && it < MAX_ITERATIONS; it++) {
            double curAlpha = getAlpha(x, p);
            addIteration(x, p, curAlpha);
            prevX = x;
            x = sum(x, product(curAlpha, p));
            p = normalize(negate(gradient.apply(x)));
        }

        addIteration(x, p,  0);
        return x;
    }

}
