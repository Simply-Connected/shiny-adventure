package org.simply.connected.application.optimization.methods.slae.math;

import java.util.Arrays;

public class Math {

    public static Vector product(SquareMatrix matrix, Vector vector) {
        validateArity(matrix.getArity(), vector.getArity());

        int arity = matrix.getArity();
        double[] res = new double[arity];
        for (int i = 0; i < arity; i++) {
            double sum = 0;
            for (int j = 0; j < arity; j++) {
                sum += matrix.get(i, j) * vector.get(j);
            }
            res[i] = sum;
        }
        return new Vector(res);
    }

    public static double dotProduct(Vector a, Vector b) {
        validateArity(a.getArity(), b.getArity());
        double res = 0;
        for (int i = 0; i < a.getArity(); i++) {
            res += a.get(i) * b.get(i);
        }
        return res;
    }

    public static Vector product(double a, Vector v) {
        return new Vector(Arrays.stream(v.getData()).map(it -> it * a).toArray());
    }

    public static double norm(Vector v) {
        return java.lang.Math.sqrt(dotProduct(v, v));
    }

    public static double normSquare(Vector v) {
        return dotProduct(v, v);
    }

    public static Vector add(Vector a, Vector b) {
        validateArity(a.getArity(), b.getArity());
        int arity = a.getArity();
        double[] sum = new double[arity];
        for (int i = 0; i < arity; i++) {
            sum[i] = a.get(i) + b.get(i);
        }
        return new Vector(sum);
    }

    public static Vector subtract(Vector a, Vector b) {
        return add(a, negate(b));
    }

    public static Vector negate(Vector v) {
        return product(-1, v);
    }

    private static void validateArity(int a, int b) {
        if (a != b) {
            throw new IllegalArgumentException("Arguments should have same arity");
        }
    }
}
