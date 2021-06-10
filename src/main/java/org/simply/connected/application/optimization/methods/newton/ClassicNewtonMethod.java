package org.simply.connected.application.optimization.methods.newton;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.function.Function;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class ClassicNewtonMethod extends AbstractNewtonMethod {
    public ClassicNewtonMethod(Function function, double eps) {
        super(function, eps);
    }

    public ClassicNewtonMethod(Function function) {
        super(function);
    }

    @Override
    protected Vector step(Vector x) {
        Vector p = solveSLAE(function.hessian(x), negate(function.gradient(x)));
        if (p.getData().stream().anyMatch(e -> e.isNaN())) {
            p = negate(function.gradient(x));
        }
        return sum(x, p);
    }

}
