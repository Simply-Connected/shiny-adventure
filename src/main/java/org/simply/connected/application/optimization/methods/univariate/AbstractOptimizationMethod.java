package org.simply.connected.application.optimization.methods.univariate;

import org.simply.connected.application.optimization.methods.univariate.model.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public abstract class AbstractOptimizationMethod implements OptimizationMethod {
    protected final List<Data> iterations;

    protected UnaryOperator<Double> function;

    protected double eps;
    public AbstractOptimizationMethod(UnaryOperator<Double> function, double eps) {
        this.function = function;
        this.eps = eps;
        this.iterations = new ArrayList<>();
    }

    public UnaryOperator<Double> getFunction() {
        return function;
    }

    public void setFunction(UnaryOperator<Double> function) {
        this.function = function;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }


    @Override
    public List<Data> getIterationData() {
        return iterations;
    }

    protected void addIteration(double left, double right, double min) {
        iterations.add(new Data(left, min, right));
    }
    protected double midPoint (double a, double b) {
        return (a + b) / 2;
    }

}

