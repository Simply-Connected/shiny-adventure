package org.simply.connected.application.optimization.methods.slae;

import org.simply.connected.application.optimization.methods.slae.math.DenseMatrix;
import org.simply.connected.application.optimization.methods.slae.math.SquareMatrix;
import org.simply.connected.application.optimization.methods.slae.math.Vector;

import java.io.BufferedReader;
import java.io.IOException;

public class GaussSlaeSolver extends AbstractSlaeSolver {
    private final static double EPS = 1e-8;

    private static class RowSwappableSlae {
        final SquareMatrix matrix;
        final Vector vector;
        final int[] perm;

        RowSwappableSlae(SquareMatrix matrix, Vector vector) {
            this.matrix = matrix;
            this.vector = vector;
            int arity = matrix.getArity();
            perm = new int[arity];
            for (int i = 0; i < arity; i++) {
                perm[i] = i;
            }
        }

        double get(int i, int j) {
            return matrix.get(perm[i], j);
        }

        void set(int i, int j, double val) {
            matrix.set(perm[i], j, val);
        }

        int getArity() {
            return matrix.getArity();
        }

        void swapRows(int i, int j) {
            int tmp = perm[i];
            perm[i] = perm[j];
            perm[j] = tmp;
        }

        double getFree(int i) {
            return vector.get(perm[i]);
        }

        void setFree(int i, double val) {
            vector.set(perm[i], val);
        }
    }


    @Override
    public Vector solve(SquareMatrix coefficients, Vector freeValues) {
        RowSwappableSlae slae = diagonalize(coefficients, freeValues);
        return solveForUpperTriangular(slae);
    }

    private static Vector solveForUpperTriangular(RowSwappableSlae slae) {
        int n = slae.getArity();
        double[] res = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sumCalculated = 0;
            for (int j = n - 1; j > i; j--) {
                sumCalculated += slae.get(i, j) * res[j];
            }
            res[i] = (slae.getFree(i) - sumCalculated) / slae.get(i, i);
        }
        return new Vector(res);
    }

    private static RowSwappableSlae diagonalize(SquareMatrix coefficients, Vector freeValues) {
        RowSwappableSlae slae = new RowSwappableSlae(coefficients, freeValues);
        int arity = coefficients.getArity();
        for (int k = 0; k < arity - 1; k++) {
            if (!chooseMaxAndSwap(k, slae)) {
                throw new IllegalStateException("No unambiguous solution");
            }
            for (int i = k + 1; i < arity; i++) {
                double t = slae.get(i, k) / slae.get(k, k);
                slae.setFree(i,slae.getFree(i) - t * slae.getFree(k));
                slae.set(i, k, 0);
                for (int j = k + 1; j < arity; j++) {
                    slae.set(i, j, slae.get(i, j) - t * slae.get(k, j));
                }
            }
        }
        return slae;
    }

    private static boolean chooseMaxAndSwap(int k, RowSwappableSlae slae) {
        double max = 0;
        int maxRow = 0;
        for (int i = k + 1; i < slae.getArity(); i++) {
            double cur = Math.abs(slae.get(i, k));
            if (max < cur)  {
                max = cur;
                maxRow = i;
            }
        }
        if (max < EPS) {
            return false;
        }
        slae.swapRows(maxRow, k);
        return true;
    }

    @Override
    protected SquareMatrix readMatrix(BufferedReader reader, int arity) throws IOException {
        return DenseMatrix.readFrom(reader, arity);
    }
}
