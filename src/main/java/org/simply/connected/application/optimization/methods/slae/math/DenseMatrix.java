package org.simply.connected.application.optimization.methods.slae.math;

import org.simply.connected.application.optimization.methods.slae.generator.Utils;

import java.io.BufferedReader;
import java.io.IOException;

public class DenseMatrix implements SquareMatrix{
    private final double[][] data;

    public DenseMatrix(double[]... data) {
        this.data = data;
    }

    @Override
    public double get(int i, int j) {
        return data[i][j];
    }

    @Override
    public void set(int i, int j, double val) {
        data[i][j] = val;
    }

    @Override
    public int getArity() {
        return data.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length - 1; i++) {
            double[] row = data[i];
            sb.append(Utils.joinToString(row)).append(System.lineSeparator());
        }
        sb.append(Utils.joinToString(data[data.length - 1]));
        return sb.toString();
    }
    public static DenseMatrix readFrom(BufferedReader reader, int arity) throws IOException {
        double[][] res = new double[arity][];
        for (int i = 0; i < arity; i++) {
            res[i] = Utils.readDoubles(reader);
        }
        return new DenseMatrix(res);
    }
}
