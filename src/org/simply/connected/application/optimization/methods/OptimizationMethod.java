package org.simply.connected.application.optimization.methods;

import org.simply.connected.application.model.Data;

import java.util.List;

public interface OptimizationMethod {
    double minimize(double a, double b);

    List<Data> getIterationData();
}
