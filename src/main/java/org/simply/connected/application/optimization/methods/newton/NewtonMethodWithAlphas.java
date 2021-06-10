package org.simply.connected.application.optimization.methods.newton;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.function.Function;
import org.simply.connected.application.optimization.methods.univariate.GoldenRatioMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.product;
import static org.simply.connected.application.optimization.methods.multivariate.math.Math.sum;

public abstract class NewtonMethodWithAlphas extends AbstractNewtonMethod {
    protected final List<Double> alphas = new ArrayList<>();

    public NewtonMethodWithAlphas(Function function, double eps) {
        super(function, eps);
    }

    public NewtonMethodWithAlphas(Function function) {
        super(function);
    }


    protected double getAlpha(Vector x, Vector p) {
        return getAlpha(x, p, 0, 1);
    }
    protected double getAlpha(Vector x, Vector p, double from, double to) {
        UnaryOperator<Double> unaryFun = alpha -> function.apply(sum(x, product(alpha, p)));
        return new GoldenRatioMethod(unaryFun, getEps()).minimize(from, to);
    }


    @Override
    public Vector minimize(Vector initialPoint) {
        clearAlphas();
        return super.minimize(initialPoint);
    }

    protected void addAlpha(double a) {
        alphas.add(a);
    }

    protected void clearAlphas() {
        alphas.clear();
    }
    public List<Double> getAlphas() {
        return alphas;
    }
}
