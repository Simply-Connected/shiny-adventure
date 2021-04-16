package org.simply.connected.application.optimization.methods.multivariate.util;

import org.simply.connected.application.optimization.methods.multivariate.AbstractMultivariateOptimizationMethod;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.multivariate.model.MultivariateData;
import org.simply.connected.application.optimization.methods.univariate.*;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class MultivariateOptimizationMethodTableResearchConstructor {
    private final AbstractMultivariateOptimizationMethod optimizationMethod;

    public MultivariateOptimizationMethodTableResearchConstructor(AbstractMultivariateOptimizationMethod optimizationMethod) {
        this.optimizationMethod = optimizationMethod;
    }

    public String iterationsUsingUnivariateMethodCSV(final Vector initialPoint) {
        StringBuilder stringBuilder = new StringBuilder("Method, Iterations\n");
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
            optimizationMethod.setMethodFactory(methodFactory);
            optimizationMethod.minimize(initialPoint);
            stringBuilder.append(
                    String.format("%s, %s%n", methodNames.get(i), optimizationMethod.getIterationData().size())
            );
            i++;
        }

        optimizationMethod.setMethodFactory(oldFactory);
        return stringBuilder.toString();
    }

}
