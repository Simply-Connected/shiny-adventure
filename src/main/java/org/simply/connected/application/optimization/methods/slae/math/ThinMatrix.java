package org.simply.connected.application.optimization.methods.slae.math;

import org.simply.connected.application.optimization.methods.slae.generator.Utils;

import java.io.BufferedReader;
import java.io.IOException;

public class ThinMatrix extends AbstractCompactMatrix {
    private final int[] ia;
    private final int[] ja;

    public ThinMatrix(double[] diag, double[] aL, double[] aU, int[] ia, int[] ja) {
        super(diag, aL, aU);
        this.ia = ia;
        this.ja = ja;
    }

    @Override
    protected int getLowerInd(int i, int j) {
        return ia[i] + getJAOffset(i, j);
    }

    @Override
    protected int getUpperInd(int i, int j) {
        return getLowerInd(j, i);
    }

    @Override
    protected boolean inUpper(int i, int j) {
        return inLower(j, i);
    }

    @Override
    protected boolean inLower(int i, int j) {
            return i > j && getJAOffset(i, j) != -1;
    }

    private int getPortrait(int i) {
        return ia[i + 1] - ia[i];
    }

    private int getJAOffset(int i, int j) {
        for (int k = 0; k < getPortrait(i); k++) {
            if (ja[ia[i] + k] == j) {
                return k;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return String.format(
                "%s%n%s%n%s%n%s%n%s",
                Utils.joinToString(diag),
                Utils.joinToString(aL),
                Utils.joinToString(aU),
                Utils.joinToString(ia),
                Utils.joinToString(ja)
        );
    }

    public static ThinMatrix readFrom(BufferedReader reader, int arity) throws IOException {
        return new ThinMatrix(
                Utils.readDoubles(reader),
                Utils.readDoubles(reader),
                Utils.readDoubles(reader),
                Utils.readInts(reader),
                Utils.readInts(reader)
        );
    }
}
