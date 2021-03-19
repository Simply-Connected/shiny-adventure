package org.simply.connected.application.optimization.methods;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Class for research on optimization method
 *
 * {@link OptimizationMethodResearchTableConstructor#researchCSV(double, double)}
 * returns method's iteration data in string csv format
 *
 * }{@link OptimizationMethodResearchTableConstructor#lnToIterationsCSV(double, double)}
 * returns relation between -lg(eps) and number of iteration
 */
public class OptimizationMethodResearchTableConstructor {
    private final AbstractOptimizationMethod optimizationMethod;

    public OptimizationMethodResearchTableConstructor(AbstractOptimizationMethod optimizationMethod) {
        this.optimizationMethod = optimizationMethod;
    }

    public String researchCSV(double a, double b) {
        optimizationMethod.minimize(a, b);
        UnaryOperator<Double> function = optimizationMethod.function;

        String formatString = Stream.generate( () -> "%.10f").limit(4).collect(Collectors.joining(", "));
        return optimizationMethod.getIterationData().stream().map(
                (data) -> String.format(
                        formatString,
                        data.getLeft(),
                        data.getRight(),
                        data.getMin(),
                        function.apply(data.getMin())
                )
        ).collect(Collectors.joining(System.lineSeparator()));
    }

    public String lnToIterationsCSV(double a, double b) {
        double eps = optimizationMethod.getEps();
        double oldEps = eps;
        optimizationMethod.setEps(1);
        eps = 1d;
        StringBuilder sb = new StringBuilder("lg(eps), iterationNum\n");
        for (int i = 0; i < 8; i++) {
            eps /= 10d;
            optimizationMethod.setEps(eps);
            optimizationMethod.minimize(a, b);
            sb.append(String.format("%f, %d\n", -Math.log10(eps), optimizationMethod.getIterationData().size()));
        }
        optimizationMethod.setEps(oldEps);
        return sb.toString();
    }

}
