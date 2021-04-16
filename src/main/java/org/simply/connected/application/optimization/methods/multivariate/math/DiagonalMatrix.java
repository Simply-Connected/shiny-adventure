package org.simply.connected.application.optimization.methods.multivariate.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiagonalMatrix implements Matrix {
    private final List<Double> data;

    public DiagonalMatrix(List<Double> data) {
        this.data = data;
    }

    public DiagonalMatrix(double ... args) {
        this.data = Arrays.stream(args).boxed().collect(Collectors.toList());
    }

    @Override
    public List<Vector> getData() {
        return IntStream.range(0, data.size()).mapToObj( it ->
                new Vector(new ArrayList<>(Collections.nCopies(data.size(), 0d)).set(it, data.get(it)))
        ).collect(Collectors.toList());
    }

    @Override
    public double get(int i, int j) {
        if (i != j)
            return 0;
        return data.get(i);
    }

    @Override
    public Vector get(int i) {
        return new Vector(new ArrayList<>(Collections.nCopies(data.size(), 0d)).set(i, data.get(i)));
    }
}
