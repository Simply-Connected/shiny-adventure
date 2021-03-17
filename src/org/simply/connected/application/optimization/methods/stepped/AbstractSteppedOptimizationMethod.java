package org.simply.connected.application.optimization.methods.stepped;

import org.simply.connected.application.model.Segment;
import java.util.function.Function;


public abstract class AbstractSteppedOptimizationMethod implements SteppedOptimizationMethod {
    protected final Function<Double, Double> function;
    protected final Double eps;
    protected Segment currSegment;


    protected AbstractSteppedOptimizationMethod(Function<Double, Double> function, Double eps, Segment segment) {
        this.function = function;
        this.eps = eps;
        this.currSegment = segment;
    }

    @Override
    public Segment getCurrSegment() {
        return currSegment;
    }
}
