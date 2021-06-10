package org.simply.connected.application.optimization.methods.newton.quasi;

import org.simply.connected.application.optimization.methods.multivariate.math.Matrix;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.function.Function;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class PowellMethod extends AbstractQuasiNewtonMethod {
    public PowellMethod(Function function, double eps) {
        super(function, eps);
    }

    public PowellMethod(Function function) {
        super(function);
    }

    @Override
    protected Matrix nextG(Vector deltaW) {
        Vector x = sum(deltaX, product(g, deltaW));
        return subtract(g, product(1d / dotProduct(deltaW, x), product(x, x)));
    }
}
