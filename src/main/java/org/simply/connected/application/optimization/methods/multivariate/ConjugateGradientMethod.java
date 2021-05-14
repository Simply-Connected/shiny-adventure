package org.simply.connected.application.optimization.methods.multivariate;

import org.simply.connected.application.optimization.methods.multivariate.math.QuadraticFunction;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.univariate.OptimizationMethod;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class ConjugateGradientMethod extends AbstractMultivariateOptimizationMethod {


    public ConjugateGradientMethod(QuadraticFunction function,
                                      double eps,
                                      BiFunction<UnaryOperator<Double>, Double, OptimizationMethod> methodFactory) {
        super(function, eps, methodFactory);
    }

    public ConjugateGradientMethod(QuadraticFunction function, double eps) {
        super(function, eps);
    }

    @Override
    public Vector minimize(final Vector initialPoint) {
        iterationData.clear();

        Vector x = initialPoint;

        int arity = x.getArity();
        Function<Vector, Vector> gradient = getGradient();
        Vector Gx = gradient.apply(x);
        Vector p = negate(Gx);

        for (int i = 1; norm(p) >= EPS && i <= MAX_ITERATIONS; i++) {
            Vector Ap = product(function.getA(), p);
            double curAlpha = normSquare(Gx) / dotProduct(Ap, p);
            Vector GxNext = sum(Gx, product(curAlpha, Ap));

            addIteration(x, p, curAlpha);

            x = sum(x, product(curAlpha, p));
            double beta = 0;
            if (i % arity != 0) {
                beta = normSquare(GxNext) / normSquare(Gx);
            }
            p = sum(negate(GxNext), product(beta, p));
            Gx = GxNext;
        }
        addIteration(x, p, 0);
        return x;
    }


}
