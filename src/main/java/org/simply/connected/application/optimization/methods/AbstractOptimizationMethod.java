package org.simply.connected.application.optimization.methods;

import org.simply.connected.application.model.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public abstract class AbstractOptimizationMethod implements OptimizationMethod {
    protected final List<Data> dataList;
    protected final UnaryOperator<Double> function;
    protected final double eps;

    public AbstractOptimizationMethod(UnaryOperator<Double> function, double eps) {
        this.function = function;
        this.eps = eps;
        this.dataList = new ArrayList<>();
    }

    @Override
    public List<Data> getIterationData() {
        return dataList;
    }
}
