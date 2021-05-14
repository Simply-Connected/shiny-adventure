package org.simply.connected.application.optimization.methods.multivariate.util;

import org.simply.connected.application.optimization.methods.multivariate.AbstractMultivariateOptimizationMethod;
import org.simply.connected.application.optimization.methods.multivariate.math.QuadraticFunction;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.multivariate.model.MultivariateData;
import org.simply.connected.application.optimization.methods.univariate.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class MultivariateOptimizationMethodTableResearchConstructor {
    private final AbstractMultivariateOptimizationMethod optimizationMethod;

    public MultivariateOptimizationMethodTableResearchConstructor(AbstractMultivariateOptimizationMethod optimizationMethod) {
        this.optimizationMethod = optimizationMethod;
    }

    public String iterationsUsingUnivariateMethodCSV(final Vector initialPoint) {
        StringBuilder stringBuilder = new StringBuilder("Method, MultivariateIterations, UnivariateIterationSum\n");
        List<BiFunction<UnaryOperator<Double>, Double, OptimizationMethod>> factories = List.of(
                DichotomyMethod::new,
                GoldenRatioMethod::new,
                FibonacciMethod::new,
                ParabolicMethod::new,
                BrentsMethod::new
        );
        List<String> methodNames = List.of(
                "Dichotomy", "GoldenRatio", "Fibonacci", "Parabolic", "Brents"
        );
        var oldFactory = optimizationMethod.getMethodFactory();

        int i = 0;
        for (var methodFactory: factories) {
            optimizationMethod.setUnaryMethodIterations(0);
            optimizationMethod.setMethodFactory(methodFactory);
            optimizationMethod.minimize(initialPoint);
            stringBuilder.append(
                    String.format("%s, %s, %d%n",
                            methodNames.get(i),
                            optimizationMethod.getIterationData().size(),
                            optimizationMethod.getUnaryMethodIterations()
                    )
            );
            i++;
        }

        optimizationMethod.setMethodFactory(oldFactory);
        return stringBuilder.toString();
    }

    public String iterationsUsingGeneratedFunctionsCSV() {
        QuadraticFunction oldFun = optimizationMethod.getFunction();

        StringBuilder res = new StringBuilder("F,");
        List<Integer> condNumbers = List.of(1, 2, 5, 7, 10, 20, 30, 40, 70, 100, 200, 300, 500, 1000, 1500, 2000);
        for (int cond: condNumbers) {
            res.append(cond).append(",");
        }
        res.append('\n');


        for (int arity = 10; arity <= 10_000; arity *= 10) {
            List<Integer> iterations = new ArrayList<>();
            for (int cond : condNumbers) {
                QuadraticFunction fun = QuadraticFunctionGenerator.generate(arity, cond);
                optimizationMethod.setFunction(fun);
                optimizationMethod.minimize(Vector.of(arity, 0));
                iterations.add(optimizationMethod.getIterationData().size());
            }
            res.append(arity)
                    .append(", ")
                    .append(iterations.stream().map(Objects::toString).collect(Collectors.joining(",")))
                    .append('\n');
        }
        optimizationMethod.setFunction(oldFun);

        return res.toString();
    }

}
