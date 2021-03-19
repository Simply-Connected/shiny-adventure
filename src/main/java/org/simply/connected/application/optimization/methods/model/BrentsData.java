package org.simply.connected.application.optimization.methods.model;

public class BrentsData extends Data {
    private final boolean isParabolicIteration;

    public BrentsData(double a, double b, double c, boolean f) {
        super(a, b, c);
        isParabolicIteration = f;
    }

    public boolean isParabolicIteration() {
        return isParabolicIteration;
    }
}
