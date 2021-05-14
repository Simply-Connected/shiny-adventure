package org.simply.connected.application.optimization.methods.multivariate;


import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.multivariate.model.MultivariateData;

import java.util.List;

public interface MultivariateOptimizationMethod {
    Vector minimize(Vector x);

    List<MultivariateData> getIterationData();
}
