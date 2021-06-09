package org.simply.connected.application.optimization.methods.slae.math;

public interface SquareMatrix {
    double get(int i, int j);

    void set(int i, int j, double val);

    int getArity();
}
