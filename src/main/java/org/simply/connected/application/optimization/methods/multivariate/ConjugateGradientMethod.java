package org.simply.connected.application.optimization.methods.multivariate;

import org.simply.connected.application.optimization.methods.multivariate.math.QuadraticFunction;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.univariate.OptimizationMethod;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class ConjugateGradientMethod extends AbstractMultivariateOptimizationMethod {


    protected ConjugateGradientMethod(Function<Vector, Double> function,
                                      double eps,
                                      BiFunction<UnaryOperator<Double>, Double, OptimizationMethod> methodFactory) {
        super(function, eps, methodFactory);
    }

    protected ConjugateGradientMethod(Function<Vector, Double> function, double eps) {
        super(function, eps);
    }

    @Override
    public Vector minimize(final Vector initialPoint) {
        iterationData.clear();

        Vector x = initialPoint;
        boolean isQuadratic = false;
        if (function instanceof QuadraticFunction) {
            isQuadratic = true;
        }

        int arity = x.getArity();
        Function<Vector, Vector> gradient = getGradient();
        Vector Gx = gradient.apply(x);
        Vector p = negate(Gx);

        int i = 1;
        while (norm(p) >= EPS) {
            double curAlpha;
            Vector Ap;
            Vector GxNext = null;

            if (isQuadratic) {
                Ap = product(((QuadraticFunction) function).getA(), p);
                curAlpha = normSquare(Gx) / dotProduct(Ap, p);
                GxNext = sum(Gx, product(curAlpha, Ap));
            } else {
                curAlpha = getAlpha(x, p);
            }

            addIteration(x, p, curAlpha);

            x = sum(x, product(curAlpha, p));
            if (!isQuadratic) {
                GxNext = gradient.apply(x);
            }
            double beta = 0;
            if (i % arity == 0) {
                beta = normSquare(GxNext) / normSquare(Gx);
            }
            p = sum(negate(GxNext), product(beta, p));
            Gx = GxNext;
            i++;
        }
        addIteration(x, p, 0);
        return x;
    }


}
