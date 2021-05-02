package org.simply.connected.application.optimization.methods.multivariate;

import org.simply.connected.application.optimization.methods.multivariate.math.Math;
import org.simply.connected.application.optimization.methods.multivariate.math.QuadraticFunction;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.multivariate.model.MultivariateData;
import org.simply.connected.application.optimization.methods.univariate.BrentsMethod;
import org.simply.connected.application.optimization.methods.univariate.OptimizationMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.product;
import static org.simply.connected.application.optimization.methods.multivariate.math.Math.sum;

public abstract class AbstractMultivariateOptimizationMethod implements MultivariateOptimizationMethod {
    protected static final int MAX_ITERATIONS = 2000;

    protected BiFunction<UnaryOperator<Double>, Double, OptimizationMethod> methodFactory = null;

    protected final List<MultivariateData> iterationData;

    protected QuadraticFunction function;

    protected double EPS;

    protected AbstractMultivariateOptimizationMethod(QuadraticFunction function, double eps) {
        this.function = function;
        EPS = eps;
        iterationData = new ArrayList<>();
    }

    protected AbstractMultivariateOptimizationMethod(QuadraticFunction function,
                                    double eps,
                                    BiFunction<UnaryOperator<Double>, Double, OptimizationMethod> methodFactory) {
        this(function, eps);
        this.methodFactory = methodFactory;
    }
    abstract public Vector minimize(Vector x);

    public Function<Vector, Double> getFunction() {
        return function;
    }

    public void setFunction(QuadraticFunction function) {
        this.function = function;
    }

    public double getEPS() {
        return EPS;
    }

    public void setEPS(double EPS) {
        this.EPS = EPS;
    }

    public List<MultivariateData> getIterationData() {
        return iterationData;
    }

    public BiFunction<UnaryOperator<Double>, Double, OptimizationMethod> getMethodFactory() {
        return methodFactory;
    }

    public void setMethodFactory(BiFunction<UnaryOperator<Double>, Double, OptimizationMethod> methodFactory) {
        this.methodFactory = methodFactory;
    }

    protected void addIteration(Vector x, Vector p, double alpha) {
        iterationData.add(new MultivariateData(x, p, alpha));
    }

    protected Function<Vector, Vector> getGradient() {
        return Math.gradient(function);
    }

    protected double getStep(Vector x, Vector p) {
        UnaryOperator<Double> univariateFun = alpha -> function.apply(sum(x, product(alpha, p)));
        OptimizationMethod method;
        if (methodFactory == null) {
            method = new BrentsMethod(univariateFun, 10 * EPS);
        } else  {
            method = methodFactory.apply(univariateFun, EPS);
        }
        return method.minimize(0,  2d / function.getMaxEigenValue());
    }
}
