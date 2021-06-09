package org.simply.connected.application.optimization.methods.slae.generator;

import org.simply.connected.application.optimization.methods.slae.math.ProfileMatrix;
import org.simply.connected.application.optimization.methods.slae.math.SquareMatrix;

import java.util.List;

public class HilbertMatrixGenerator extends AbstractSlaeGenerator {
    private static final List<Integer> arities =
            List.of(2, 3, 5, 10, 15, 20, 30, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000);

    @Override
    protected String getFileName(int arity, int matrixInd, String generationID) {
        return String.format("%s_%d", generationID, arity);
    }

    @Override
    protected List<SquareMatrix> getMatrices(int arity) {
        int[] profiles = getProfiles(arity);
        double[] aL = getTriangle(arity, profiles);
        double[] aU = getTriangle(arity, profiles);
        int[] inf = getInf(arity, profiles);
        double[] diag = getDiagonal(arity);
        return List.of(new ProfileMatrix(diag, aL, aU, inf));
    }

    @Override
    protected List<Integer> getArities() {
        return arities;
    }

    private int[] getProfiles(int arity) {
        int[] res = new int[arity];
        for (int i = 0; i < arity; i++) {
            res[i] = i;
        }
        return res;
    }

    private int[] getInf(int arity, int[] profiles) {
        int[] res = new int[arity + 1];
        res[0] = 0;
        res[1] = 0;
        for (int i = 2; i < arity + 1; i++) {
            res[i] = res[i - 1] + profiles[i - 1];
        }
        return res;
    }


    private double[] getDiagonal(int arity) {
        double[] res = new double[arity];
        for (int i = 0; i < arity; i++) {
            res[i] = 1d / (i + i + 1);
        }
        return res;
    }

    private double[] getTriangle(int arity, int[] profiles) {
        double[] res = new double[Utils.sum(profiles)];
        int curIndex = 0;
        for (int i = 0; i < arity; i++) {
            for (int j = 0; j < profiles[i]; j++) {
                res[curIndex++] = 1d / (i + j + 1);
            }
        }
        return res;
    }

}
