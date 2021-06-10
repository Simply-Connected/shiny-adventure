package org.simply.connected.application.optimization.methods.newton.expression;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Sum implements Expression {
    private final Expression[] args;

    public Sum(Expression... args) {
        this.args = args;
    }

    @Override
    public double eval(double... vars) {
        return Arrays.stream(args).mapToDouble(e -> e.eval(vars)).sum();
    }

    @Override
    public Expression der(int varInd) {
        return new Sum(Arrays.stream(args).map(e -> e.der(varInd)).toArray(Expression[]::new));
    }

    @Override
    public String toPythonString() {
        return String.format("((%s))",
                Arrays.stream(args).map(Expression::toPythonString).collect(Collectors.joining(") + (")));
    }

    @Override
    public String toString() {
        return String.format("((%s))",
                Arrays.stream(args).map(Expression::toString).collect(Collectors.joining(") + (")));
    }
}
