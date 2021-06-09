package org.simply.connected.application.optimization.methods.slae;

import org.simply.connected.application.optimization.methods.slae.math.SquareMatrix;
import org.simply.connected.application.optimization.methods.slae.math.ThinMatrix;
import org.simply.connected.application.optimization.methods.slae.math.Vector;

import java.io.BufferedReader;
import java.io.IOException;

import static org.simply.connected.application.optimization.methods.slae.math.Math.*;

public class ConjugateGradientSlaeSolver extends AbstractSlaeSolver {
    private static final int MAX_ITERATIONS = 2000;
    private final static double EPS = 1e-8;
    private int iterations = 0;

    @Override
    public Vector solve(SquareMatrix A, Vector f) {
        Vector x = new Vector(f.getData());
        Vector r = subtract(f, product(A, x));
        Vector z = new Vector(r.getData());
        for (iterations = 0; iterations < MAX_ITERATIONS && norm(r) / norm(f) >= EPS; iterations++) {
            Vector Az = product(A, z);
            double alpha = normSquare(r) / dotProduct(Az, z);
            x = add(x, product(alpha, z));
            Vector nextR = subtract(r, product(alpha, Az));
            double beta = normSquare(nextR) / normSquare(r);
            z = add(nextR, product(beta, z));
            r = nextR;
        }
        return x;
    }


    @Override
    protected SquareMatrix readMatrix(BufferedReader reader, int arity) throws IOException {
        return ThinMatrix.readFrom(reader, arity);
    }

    public int getIterations() {
        return iterations;
    }

}
