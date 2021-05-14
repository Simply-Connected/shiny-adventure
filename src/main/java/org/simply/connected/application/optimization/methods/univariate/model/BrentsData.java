package org.simply.connected.application.optimization.methods.univariate.model;

public class BrentsData extends TernaryData {
    private final boolean isParabolicIteration;

    public BrentsData(double a, double min, double c, double x1, double x2, boolean parabolic) {
        super(a, min, c, x1 , x2);
        isParabolicIteration = parabolic;
    }

    public boolean isParabolicIteration() {
        return isParabolicIteration;
    }
}
