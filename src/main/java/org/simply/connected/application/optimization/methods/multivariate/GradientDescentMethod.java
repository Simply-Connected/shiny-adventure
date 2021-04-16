package org.simply.connected.application.optimization.methods.multivariate;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;

import java.util.function.Function;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class GradientDescentMethod extends AbstractMultivariateOptimizationMethod {
    private double alpha;

    public GradientDescentMethod(Function<Vector, Double> function, double eps, double alpha) {
        super(function, eps);
        this.alpha = alpha;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public Vector minimize(final Vector initialPoint) {
        iterationData.clear();

        Vector x = initialPoint;
        double fX = function.apply(x);
        double curAlpha = alpha;
        Function<Vector, Vector> gradient = getGradient();
        Vector p = negate(gradient.apply(x));

        addIteration(x, p,  curAlpha);

        while (norm(p) >= EPS) {
            Vector y = sum(x, product(curAlpha / norm(p), p));
            double fY = function.apply(y);
            while(fY >= fX) {
                curAlpha /= 2;
                y = sum(x, product(curAlpha / norm(p), p));
                fY = function.apply(y);
                addIteration(x, p, curAlpha);
            }

            x = y;
            p = negate(gradient.apply(x));
            fX = fY;
            addIteration(x, p, curAlpha);
        }
        return x;
    }
}
