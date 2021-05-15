package org.simply.connected.application.optimization.methods.multivariate.model;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;

public class MultivariateData {
    private final Vector x;
    private final double y;

    public MultivariateData(Vector x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("{%s %.6f}", x, y);
    }
}
