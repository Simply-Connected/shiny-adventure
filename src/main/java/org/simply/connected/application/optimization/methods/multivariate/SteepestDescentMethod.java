package org.simply.connected.application.optimization.methods.multivariate;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.univariate.OptimizationMethod;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class SteepestDescentMethod extends AbstractMultivariateOptimizationMethod {

    protected SteepestDescentMethod(Function<Vector, Double> function, double eps) {
        super(function, eps);
    }

    protected SteepestDescentMethod(Function<Vector, Double> function,
                                    double eps,
                                    BiFunction<UnaryOperator<Double>, Double, OptimizationMethod> methodFactory) {
        super(function, eps, methodFactory);
    }

    @Override
    public Vector minimize(final Vector initalPoint) {
        iterationData.clear();

        Vector x = initalPoint;
        double fX = function.apply(x);
        Function<Vector, Vector> gradient = getGradient();
        Vector p = negate(gradient.apply(x));


        while (norm(p) >= EPS) {
            double curAlpha = getAlpha(x, p);
            addIteration(x, p, curAlpha);
            x = sum(x, product(curAlpha / norm(p), p));
            p = negate(gradient.apply(x));
        }

        addIteration(x, p,  0);
        return x;
    }

}
