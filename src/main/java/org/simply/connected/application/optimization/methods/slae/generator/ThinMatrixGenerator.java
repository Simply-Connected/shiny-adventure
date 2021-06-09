package org.simply.connected.application.optimization.methods.slae.generator;

import org.simply.connected.application.optimization.methods.slae.math.SquareMatrix;
import org.simply.connected.application.optimization.methods.slae.math.ThinMatrix;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;
import static java.lang.Math.round;

public class ThinMatrixGenerator extends AbstractSlaeGenerator {
    private static final List<Integer> arities =
            List.of(10, 20, 30, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000);

    private final double maxPortraitSizeRatio;
    private final boolean isPositive;
    private final Random random;

    public ThinMatrixGenerator(double maxPortraitSizeRatio, boolean isPositive) {
        this.isPositive = isPositive;
        if (maxPortraitSizeRatio <= 0 || maxPortraitSizeRatio > 1) {
            throw new IllegalStateException("Max Portrait size should be between 0 and 1");
        }
        this.maxPortraitSizeRatio = maxPortraitSizeRatio;
        random = new Random(new Date().getTime());
    }

    protected int[] getInf(int arity, int[] portrait) {
        int[] res = new int[arity + 1];
        res[0] = 0;
        res[1] = 0;
        for (int i = 2; i < arity + 1; i++) {
            res[i] = res[i - 1] + portrait[i - 1];
        }
        return res;
    }


    private int[] getJA(int arity, int[] portrait) {
        int[] res = new int[Utils.sum(portrait)];
        int ind = 0;
        for (int i = 0; i < arity; i++) {
            for (int rand: random.ints(0, i + 1).distinct().limit(portrait[i]).sorted().toArray()) {
                res[ind++] = rand;
            }
        }
        return res;
    }

    protected int[] getPortrait(int arity) {
        int[] res = new int[arity];
        int maxPortrait = (int) round(arity * maxPortraitSizeRatio);

        for (int i = 0; i < arity; i++) {
            res[i] = 1 + random.nextInt(min(i, maxPortrait) + 1);
        }
        return res;
    }


    protected double[] getTriangle(int arity, int[] portrait) {
        double[] res = new double[Utils.sum(portrait)];
        int curIndex = 0;
        int sign = isPositive? 1 : -1;
        for (int i = 0; i < arity; i++) {
            for (int j = 0; j < portrait[i]; j++) {
                res[curIndex++] = sign * (random.nextInt(4) + 1);
            }
        }
        return res;
    }

    @Override
    protected String getFileName(int arity, int matrixInd, String generationID) {
        return String.format("%s_%d", generationID, arity);
    }

    @Override
    protected List<SquareMatrix> getMatrices(int arity) {
        List<SquareMatrix> res = new ArrayList<>();
        int[] portrait = getPortrait(arity);
        int[] inf = getInf(arity, portrait);
        int[] ja = getJA(arity, portrait);
        double[] aL = getTriangle(arity, portrait);
        double[] diag = new double[arity];
        ThinMatrix matrix = new ThinMatrix(diag, aL, aL, inf, ja);
        for (int i = 0; i < arity; i++) {
            matrix.set(i, i, -getRowSum(matrix, i));
        }
        matrix.set(0, 0, matrix.get(0, 0) + 1);
        res.add(matrix);
        return res;
    }

    @Override
    protected List<Integer> getArities() {
        return arities;
    }
}
