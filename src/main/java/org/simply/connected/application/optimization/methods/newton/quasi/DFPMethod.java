package org.simply.connected.application.optimization.methods.newton.quasi;

import org.simply.connected.application.optimization.methods.multivariate.math.Matrix;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.function.Function;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public class DFPMethod extends AbstractQuasiNewtonMethod {
    public DFPMethod(Function function, double eps) {
        super(function, eps);
    }

    public DFPMethod(Function function) {
        super(function);
    }

    @Override
    protected Matrix nextG(Vector deltaW) {
        Vector v = product(g, deltaW);
        return subtract(
                subtract(
                        g,
                        product(1 / dotProduct(deltaW, deltaX), product(deltaX, deltaX))
                ),
                product(1 / dotProduct(deltaW, v), product(v, v))
        );
    }
}
