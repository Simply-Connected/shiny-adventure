package org.simply.connected.application.optimization.methods.multivariate.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class DiagonalMatrix implements Matrix {
    private final List<Double> data;

    public DiagonalMatrix(List<Double> data) {
        this.data = data;
    }

    public DiagonalMatrix(double... args) {
        this.data = Arrays.stream(args).boxed().collect(Collectors.toList());
    }

    @Override
    public List<Vector> getData() {
        List<Double> zeroes = new ArrayList<>(Collections.nCopies(data.size(), 0d));
        return IntStream.range(0, data.size()).mapToObj(it -> {
                    if (it > 0) zeroes.set(it - 1, 0d);
                    zeroes.set(it, data.get(it));
                    return new Vector(List.copyOf(zeroes));
                }
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

    @Override
    public double getMin() {
        return getDataDoubleStream().min().orElseThrow(IllegalStateException::new);
    }

    @Override
    public double getMax() {
        return getDataDoubleStream().max().orElseThrow(IllegalStateException::new);
    }

    private DoubleStream getDataDoubleStream() {
        return data.stream().mapToDouble(Double::doubleValue);
    }


}
