package org.simply.connected.application.optimization.methods.slae;

import org.simply.connected.application.optimization.methods.slae.math.ProfileMatrix;
import org.simply.connected.application.optimization.methods.slae.math.SquareMatrix;
import org.simply.connected.application.optimization.methods.slae.math.Vector;

import java.io.BufferedReader;
import java.io.IOException;

public class LUDecompositionSlaeSolver extends AbstractSlaeSolver{

    public static void LUDecompose(SquareMatrix matrix) {
        for (int i = 1; i < matrix.getArity(); i++) {
            double sumD = 0;
            for (int j = 0; j < i; j++) {
                double sumL = 0;
                double sumU = 0;
                for (int k = 0; k < j; k++) {
                    sumL += matrix.get(i, k) * matrix.get(k, j);
                    sumU += matrix.get(j, k) * matrix.get(k, i);
                }
                set(matrix, i, j, matrix.get(i, j) - sumL);
                set(matrix, j, i, (matrix.get(j, i) - sumU) / matrix.get(j, j));

                sumD += matrix.get(i, j) * matrix.get(j, i);
            }
            set(matrix, i, i, matrix.get(i, i) - sumD);
        }
    }

    private static void set(SquareMatrix matrix, int i, int j, double val) {
        if (val == 0.0) return;
        matrix.set(i, j, val);

    }

    @Override
    public Vector solve(SquareMatrix coefficients, Vector freeValues) {
        LUDecompose(coefficients);
        Vector y = solveForLowerTriangular(coefficients, freeValues);
        return solveForUpperTriangular(coefficients, y);
    }

    @Override
    protected SquareMatrix readMatrix(BufferedReader reader, int arity) throws IOException {
        return ProfileMatrix.readFrom(reader, arity);
    }

    private Vector solveForLowerTriangular(SquareMatrix triangularMatrix, Vector freeValues) { //TODO Extract
        int n = triangularMatrix.getArity();
        double[] res = new double[n];
        for (int i = 0; i < n; i++) {
            double sumCalculated = 0;
            for (int j = 0; j < i; j++) {
                sumCalculated += triangularMatrix.get(i, j) * res[j];
            }
            res[i] = (freeValues.get(i) - sumCalculated) / triangularMatrix.get(i, i);
        }
        return new Vector(res);
    }

    private Vector solveForUpperTriangular(SquareMatrix triangularMatrix, Vector freeValues) {
        int n = triangularMatrix.getArity();
        double[] res = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sumCalculated = 0;
            for (int j = n - 1; j > i; j--) {
                sumCalculated += triangularMatrix.get(i, j) * res[j];
            }
            res[i] = (freeValues.get(i) - sumCalculated);
        }
        return new Vector(res);
    }

}
