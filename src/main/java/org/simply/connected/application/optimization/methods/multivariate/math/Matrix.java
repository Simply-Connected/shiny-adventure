package org.simply.connected.application.optimization.methods.multivariate.math;

import java.util.List;

public interface Matrix {

    List<Vector> getData();
    double get(int i, int j);

    Vector get(int i);

    double getMin();
    double getMax();

    boolean isDiagonal();
    Vector getDiagonal();
}
