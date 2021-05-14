package org.simply.connected.application.optimization.methods.multivariate.util;

import org.simply.connected.application.optimization.methods.multivariate.math.DiagonalMatrix;
import org.simply.connected.application.optimization.methods.multivariate.math.QuadraticFunction;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class QuadraticFunctionGenerator {
    private static final int X_OFFSET_RADIUS = 20;
    private static final int Y_OFFSET_RADIUS = 20;

    public static QuadraticFunction generate(int arity, int conditionNumber) {
        if (conditionNumber < 1)
            throw new IllegalArgumentException("Condition number should be greater than 0");
        if (arity <= 0)
            throw new IllegalArgumentException("Arity should be positive");

        Random random = new Random(new Date().getTime());

        ArrayList<Double> A = new ArrayList<>(Collections.nCopies(arity, 1d));
        A.set(arity - 1, (double) conditionNumber);

        for (int i = 1; i < arity - 1; i++) {
            A.set(i, 1 + random.nextDouble() * (conditionNumber - 1));
        }

        ArrayList<Double> B = new ArrayList<>(Collections.nCopies(arity, 0d));

        for (int i = 0; i < arity; i++) {
            B.set(i, random.nextDouble() * X_OFFSET_RADIUS - X_OFFSET_RADIUS / 2);
        }

        double C = random.nextDouble() * Y_OFFSET_RADIUS - Y_OFFSET_RADIUS / 2.;

        return new QuadraticFunction(
                new DiagonalMatrix(A),
                new Vector(B),
                C
        );
    }
}
