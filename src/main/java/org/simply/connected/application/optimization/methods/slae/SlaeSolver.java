package org.simply.connected.application.optimization.methods.slae;

import org.simply.connected.application.optimization.methods.slae.math.*;

import java.io.File;
import java.io.IOException;

public interface SlaeSolver {
    Vector solve(SquareMatrix coefficients, Vector freeValues);

    Vector solveFile(File input, File output) throws IOException;
}
