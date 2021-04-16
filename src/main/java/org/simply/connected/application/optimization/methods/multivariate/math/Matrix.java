package org.simply.connected.application.optimization.methods.multivariate.math;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Matrix {
    private final List<Vector> data;

    public Matrix(List<Vector> vectors) {
        validateArity(vectors.stream(), vectors.get(0).getArity());
        this.data = vectors;
    }

    public Matrix(Vector... vectors) {
        validateArity(Arrays.stream(vectors.clone()), vectors[0].getArity());
        this.data = Arrays.stream(vectors).collect(Collectors.toList());
    }
    private void validateArity(Stream<Vector> stream, int arity) {
        if (stream.anyMatch(v -> v.getArity() != arity)) {
            throw new IllegalArgumentException("All vectors should have the same arity");
        }
    }

    public List<Vector> getData() {
        return data;
    }

    public double get(int i, int j) {
        return data.get(i).get(j);
    }

    public Vector get(int i) {
        return data.get(i);
    }

    public Map.Entry<Integer, Integer> getArity() {
        return Map.entry(data.size() , data.get(0).getArity());
    }
}
