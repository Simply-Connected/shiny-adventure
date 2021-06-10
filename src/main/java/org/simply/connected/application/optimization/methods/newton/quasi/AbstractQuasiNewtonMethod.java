package org.simply.connected.application.optimization.methods.newton.quasi;

import org.simply.connected.application.optimization.methods.multivariate.math.BaseMatrix;
import org.simply.connected.application.optimization.methods.multivariate.math.DiagonalMatrix;
import org.simply.connected.application.optimization.methods.multivariate.math.Matrix;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.NewtonMethodWithAlphas;
import org.simply.connected.application.optimization.methods.newton.function.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

public abstract class AbstractQuasiNewtonMethod extends NewtonMethodWithAlphas implements QuasiNewtonOptimizationMethod {
    protected Matrix g;
    protected Vector w, deltaX, p;
    public AbstractQuasiNewtonMethod(Function function, double eps) {
        super(function, eps);
    }

    public AbstractQuasiNewtonMethod(Function function) {
        super(function);
    }

    @Override
    public Vector minimize(Vector x) {
        clearAlphas();
        clearIterations();
        g = getIdentityMatrix();
        w = negate(function.gradient(x));
        p = w;
        double alpha = getAlpha(x, p);
        addAlpha(alpha);
        Vector xNext = sum(x, product(alpha, p));
        deltaX = subtract(xNext, x);
        addIteration(x, function.apply(x));
        return super.iterate(xNext);
    }

    @Override
    protected Vector step(Vector x) {
        Vector wNext = negate(function.gradient(x));
        Vector deltaW = subtract(wNext, w);
        w = wNext;
        g = nextG(deltaW);
        p = product(g, w);
        double alpha = getAlpha(x, p, -1, 1);
        addAlpha(alpha);
        Vector xNext = sum(x, product(alpha, p));
        deltaX = subtract(xNext, x);
        return xNext;
    }

    protected abstract Matrix nextG(Vector deltaW);

    private Matrix getIdentityMatrix() {
        int arity = function.getArity();
        double[][] identity = new double[arity][arity];
        for (int i = 0; i < arity; i++) {
            identity[i][i] = 1;
        }
        return new BaseMatrix(Arrays.stream(identity).map(Vector::new).collect(Collectors.toList()));
    }
}
