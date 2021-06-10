package org.simply.connected.application.optimization.methods.newton.function;

import org.simply.connected.application.optimization.methods.multivariate.math.Matrix;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;

public interface Function {
    int getArity();

    Vector gradient(Vector x);
    Matrix hessian(Vector x);

    String toPythonString();
}
