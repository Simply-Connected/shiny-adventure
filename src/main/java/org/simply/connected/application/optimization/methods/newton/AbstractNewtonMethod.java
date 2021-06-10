package org.simply.connected.application.optimization.methods.newton;

import org.simply.connected.application.optimization.methods.multivariate.math.Matrix;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.function.Function;
import org.simply.connected.application.optimization.methods.newton.model.NewtonData;
import org.simply.connected.application.optimization.methods.slae.LUDecompositionSlaeSolver;
import org.simply.connected.application.optimization.methods.slae.math.DenseMatrix;

import java.util.ArrayList;
import java.util.List;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.norm;
import static org.simply.connected.application.optimization.methods.multivariate.math.Math.subtract;

public abstract class AbstractNewtonMethod implements NewtonOptimizationMethod {
    private static final double DEFAULT_EPS = 1e-8;
    private static final int MAX_ITERATIONS_PER_ARITY = 100;

    private final List<NewtonData> iterationData;
    protected Function function;
    private double eps;

    public AbstractNewtonMethod(Function function, double eps) {
        this.function = function;
        this.eps = eps;
        iterationData = new ArrayList<>();
    }

    public AbstractNewtonMethod(Function function) {
        this(function, DEFAULT_EPS);
    }


    protected abstract Vector step(Vector x);

    @Override
    public Vector minimize(Vector initialPoint) {
        clearIterations();
        return iterate(initialPoint);
    }

    protected Vector iterate(Vector initialPoint) {
        int maxIterations = MAX_ITERATIONS_PER_ARITY * function.getArity();
        Vector x = initialPoint;
        Vector xNext = x;
        addIteration(x, function.apply(x));
        do {
            x = xNext;
            xNext = step(xNext);
            addIteration(x, function.apply(x));
        } while (norm(subtract(xNext, x)) > eps && maxIterations-- > 0);

        return xNext;
    }

    protected void clearIterations() {
        iterationData.clear();
    }

    protected void addIteration(Vector x, double fX) {
        iterationData.add(new NewtonData(x, fX));
    }

    @Override
    public int getIterationCount() {
        return iterationData.size() - 1;
    }

    protected static Vector solveSLAE(Matrix matrix, Vector freeValues) {
        return new Vector(new LUDecompositionSlaeSolver()
                .solve(
                        new DenseMatrix(matrix.toArray()),
                        new org.simply.connected.application.optimization.methods.slae.math.Vector(freeValues.toArray())
                )
                .getData());
    }

    @Override
    public List<NewtonData> getIterationData() {
        return iterationData;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }
}
