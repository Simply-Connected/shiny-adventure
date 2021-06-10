package org.simply.connected.application.optimization.methods.newton.model;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;

public class NewtonData {
    private final Vector x;
    private final double Fx;

    public NewtonData(Vector x, double fx) {
        this.x = x;
        Fx = fx;
    }

    public Vector getX() {
        return x;
    }

    public double getFx() {
        return Fx;
    }
}
