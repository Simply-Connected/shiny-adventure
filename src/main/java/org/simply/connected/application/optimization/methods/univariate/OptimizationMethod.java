package org.simply.connected.application.optimization.methods.univariate;

import org.simply.connected.application.optimization.methods.univariate.model.Data;

import java.util.List;

public interface OptimizationMethod {
    double minimize(double a, double b);

    List<Data> getIterationData();
}
