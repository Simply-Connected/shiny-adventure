package org.simply.connected.application.optimization.methods.slae.math;

public abstract class AbstractCompactMatrix implements SquareMatrix {
    protected final double[] diag;
    protected final double[] aL;
    protected final double[] aU;

    protected AbstractCompactMatrix(double[] diag, double[] aL, double[] aU) {
        this.diag = diag;
        this.aL = aL;
        this.aU = aU;
    }

    @Override
    public double get(int i, int j) {
        if (i == j) {
            return diag[i];
        }
        if (inUpper(i, j)) {
            return aU[getUpperInd(i, j)];
        }
        if (inLower(i, j)) {
            return aL[getLowerInd(i, j)];
        }
        return 0;
    }

    @Override
    public void set(int i, int j, double val) {
        if (!inUpper(i, j) && !inLower(i, j) && i != j) {
            throw new IllegalStateException(String.format("Cannot modify element: (%d, %d)", i, j));
        }
        if (i == j) {
            diag[i] = val;
        }
        if (inUpper(i, j)) {
            aU[getUpperInd(i, j)] = val;
        }
        if (inLower(i, j)) {
            aL[getLowerInd(i, j)] = val;
        }
    }

    protected abstract int getLowerInd(int i, int j);

    protected abstract boolean inLower(int i, int j);

    protected abstract int getUpperInd(int i, int j);

    protected abstract boolean inUpper(int i, int j);

    @Override
    public int getArity() {
        return diag.length;
    }
}
