package org.simply.connected.application.optimization.methods.newton;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.function.Function;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;
import static org.simply.connected.application.optimization.methods.multivariate.math.Math.product;

public class SteepestDescentDirectionNewtonMethod extends NewtonMethodWithAlphas {
    public SteepestDescentDirectionNewtonMethod(Function function, double eps) {
        super(function, eps);
    }

    public SteepestDescentDirectionNewtonMethod(Function function) {
        super(function);
    }

    @Override
    protected Vector step(Vector x) {
        Vector p = solveSLAE(function.hessian(x), negate(function.gradient(x)));
        if (p.getData().stream().anyMatch(e -> e.isNaN()) || dotProduct(p, function.gradient(x)) > 0) {
            p = negate(function.gradient(x));
        }
        double alpha = getAlpha(x, p);
        addAlpha(alpha);
        return sum(x, product(alpha, p));
    }
}
