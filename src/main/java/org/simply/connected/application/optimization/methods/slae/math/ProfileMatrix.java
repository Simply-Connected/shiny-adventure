package org.simply.connected.application.optimization.methods.slae.math;

import org.simply.connected.application.optimization.methods.slae.generator.Utils;

import java.io.BufferedReader;
import java.io.IOException;

public class ProfileMatrix extends AbstractCompactMatrix {
    private final int[] ia;

    public ProfileMatrix(double[] diag, double[] aL, double[] aU, int[] inf) {
        super(diag, aL, aU);
        this.ia = inf;
    }

    @Override
    public int getArity() {
        return diag.length;
    }

    @Override
    protected int getLowerInd(int i, int j) {
        return ia[i] + j + getProfile(i) - i;
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
        return i > j && j >= i - getProfile(i);
    }

    private int getProfile(int i) {
        return ia[i + 1] - ia[i];
    }

    @Override
    public String toString() {
        return String.format(
                "%s%n%s%n%s%n%s",
                Utils.joinToString(diag),
                Utils.joinToString(aL),
                Utils.joinToString(aU),
                Utils.joinToString(ia)
        );
    }

    public static ProfileMatrix readFrom(BufferedReader reader, int arity) throws IOException {
        return new ProfileMatrix(
                Utils.readDoubles(reader),
                Utils.readDoubles(reader),
                Utils.readDoubles(reader),
                Utils.readInts(reader)
        );
    }
}
