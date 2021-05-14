package org.simply.connected.application.optimization.methods.multivariate.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Math {

    public static Vector product(double c, Vector v) {
        return new Vector(v.getData().stream().map(el -> el * c).collect(Collectors.toList()));
    }

    public static Vector negate(Vector v) {
        return product(-1, v);
    }

    public static double norm(Vector v) {
        return java.lang.Math.sqrt(v.getData().stream().mapToDouble(it -> it * it).sum());
    }

    public static double normSquare(Vector v) {
        return dotProduct(v, v);
    }

    public static double dotProduct(Vector a, Vector b) {
        validateArity(a, b);
        return IntStream.range(0, a.getArity()).mapToDouble(it -> a.get(it) * b.get(it)).sum();
    }

    public static Vector normalize(Vector v) {
        return product(1 / norm(v), v);
    }

    public static double distance(Vector a, Vector b) {
        return norm(sum(a, negate(b)));
    }

    public static Vector sum(Vector a, Vector b) {
        validateArity(a, b);
        return new Vector(
                IntStream
                        .range(0, a.getArity())
                        .mapToDouble(it -> a.get(it) + b.get(it))
                        .boxed()
                        .collect(Collectors.toList())
        );
    }

    private static void validateArity(Vector a, Vector b) {
        if (a.getArity() != b.getArity()) {
            throw new IllegalArgumentException("Vectors should have the same arity");
        }
    }

    /**
     * Matrix is list of vectors-rows. Vector is column;
     */
    public static Vector product(Matrix a, Vector v) {
        if (a.isDiagonal()) {
            Vector diagonal = a.getDiagonal();
            return new Vector(
                    IntStream.range(0, v.getArity()).mapToDouble(it -> diagonal.get(it) * v.get(it))
                            .boxed().collect(Collectors.toList())
            );
        }
        return new Vector(a.getData().stream().map(vec -> dotProduct(vec, v)).collect(Collectors.toList()));
    }

    public static Function<Vector, Vector> gradient(QuadraticFunction f) {
        return (v -> sum(product(f.getA(), v), f.getB()));
    }

    public static Function<Vector, Vector> gradient(Function<Vector, Double> f, double eps) {
        return v -> {
            int arity = v.getArity();
            ArrayList<Double> res = new ArrayList<>();
            List<Double> e = new ArrayList<>(Collections.nCopies(arity, 0D));
            for (int i = 0; i < arity; i++) {
                if (i > 0) e.set(i - 1, 0D);
                e.set(i, 1D);
                res.add((f.apply(sum(v, product(eps, new Vector(e)))) - f.apply(v)) / eps);
            }
            return new Vector(res);
        };
    }
}
