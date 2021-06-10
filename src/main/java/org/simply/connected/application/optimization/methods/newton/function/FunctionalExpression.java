package org.simply.connected.application.optimization.methods.newton.function;

import org.simply.connected.application.optimization.methods.multivariate.math.BaseMatrix;
import org.simply.connected.application.optimization.methods.multivariate.math.Matrix;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.expression.Expression;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FunctionalExpression implements Function {
    private final Expression expression;
    private final int arity;
    private final List<Expression> gradient;
    private final List<List<Expression>> hessian;

    public FunctionalExpression(Expression expression, int arity) {
        this.expression = expression;
        this.arity = arity;
        this.gradient = IntStream.range(0, arity).mapToObj(expression::der).collect(Collectors.toList());
        this.hessian = IntStream.range(0, arity).mapToObj(i ->
            gradient.stream().map(e -> e.der(i)).collect(Collectors.toList())
        ).collect(Collectors.toList());
    }


    @Override
    public Vector gradient(Vector x) {
        return new Vector(gradient.stream().mapToDouble(d -> d.eval(x.toArray())).toArray());
    }

    @Override
    public Matrix hessian(Vector x) {
        double[] values = x.toArray();
        return new BaseMatrix(
            IntStream.range(0, arity).mapToObj(it ->
                    new Vector(hessian.get(it).stream().mapToDouble(d -> d.eval(values)).toArray())
            ).collect(Collectors.toList())
        );
    }

    @Override
    public String toPythonString() {
        return expression.toPythonString();
    }
}
