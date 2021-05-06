package org.simply.connected.application.optimization.methods.multivariate.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Vector {
    private final List<Double> data;

    public Vector(double ... args) {
        this.data = Arrays.stream(args).boxed().collect(Collectors.toList());
    }

    public Vector(List<Double> data) {
        this.data = data;
    }
    public static Vector of(int arity, double value) {
        return new Vector(new ArrayList<>(Collections.nCopies(arity, value)));
    }

    public List<Double> getData() {
        return data;
    }

    public double get(int i) {
        return data.get(i);
    }

    public int getArity() {
        return data.size();
    }

    @Override
    public String toString() {
        return "[" + data.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "]";
    }
}
