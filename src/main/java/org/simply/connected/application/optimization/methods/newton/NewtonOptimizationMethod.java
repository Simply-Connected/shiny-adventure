package org.simply.connected.application.optimization.methods.newton;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.model.NewtonData;

import java.util.List;

public interface NewtonOptimizationMethod {
    Vector minimize(Vector initialPoint);

    List<NewtonData> getIterationData();

    int getIterationCount();
}
