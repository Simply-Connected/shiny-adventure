package org.simply.connected.application.optimization.methods.multivariate.model;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;

public class MultivariateData {
    private final Vector x;
    private final Vector p;
    private final double alpha;

    public MultivariateData(Vector x, Vector p, double alpha) {
        this.x = x;
        this.p = p;
        this.alpha = alpha;
    }

    public Vector getP() {
        return p;
    }

    public Vector getX() {
        return x;
    }

    public double getAlpha() {
        return alpha;
    }

    @Override
    public String toString() {
        return String.format("{%s %s %.6f}", x, p, alpha);
    }
}
