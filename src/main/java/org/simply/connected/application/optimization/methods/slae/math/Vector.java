package org.simply.connected.application.optimization.methods.slae.math;

import org.simply.connected.application.optimization.methods.slae.generator.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class Vector {
    final double[] data;

    public Vector(int arity) {
        this.data = new double[arity];
    }

    public Vector(double... values) {
        this.data = values;
    }

    public static Vector of(int arity, double value) {
        double[] data = new double[arity];
        Arrays.fill(data, value);
        return new Vector(data);
    }

    public void set(int i, double val) {
        data[i] = val;
    }

    public double get(int i) {
        return data[i];
    }

    public double[] getData() {
        return data;
    }

    public int getArity() {
        return data.length;
    }

    @Override
    public String toString() {
        return Utils.joinToString(data);
    }

    public static Vector readFrom(BufferedReader reader) throws IOException {
        return new Vector(Utils.readDoubles(reader));
    }
}
