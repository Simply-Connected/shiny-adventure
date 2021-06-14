package org.simply.connected.application.optimization.methods.front.newton;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.*;
import org.simply.connected.application.optimization.methods.newton.expression.Const;
import org.simply.connected.application.optimization.methods.newton.function.Function;
import org.simply.connected.application.optimization.methods.newton.function.FunctionalExpression;
import org.simply.connected.application.optimization.methods.newton.model.NewtonData;
import org.simply.connected.application.optimization.methods.newton.quasi.DFPMethod;
import org.simply.connected.application.optimization.methods.newton.quasi.PowellMethod;


import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;


public class PythonDataGenerator {
    public static FunctionalExpression fun = new FunctionalExpression(new Const(1), 0);
    public static Map<String, AbstractNewtonMethod> methods = Map.of(
            "classic", new ClassicNewtonMethod(fun),
            "steepest", new SteepestNewtonMethod(fun),
            "direction", new SteepestDescentDirectionNewtonMethod(fun),
            "dfp", new DFPMethod(fun),
            "powell", new PowellMethod(fun)
    );

    public static void generate(String genID, String methodName, Function fun, List<Vector> initialPoints) {
        AbstractNewtonMethod method = methods.get(methodName);
        int initInd = 0;
        for (var init : initialPoints) {
            method.setFunction(fun);
            method.minimize(init);

            List<Double> alphas = List.of();
            if (method instanceof NewtonMethodWithAlphas) {
                alphas = ((NewtonMethodWithAlphas) method).getAlphas();
            }

            writeToFile(method.getIterationData(),
                    alphas,
                    String.format("%s_%s_%d.iterations", methodName, genID, initInd),
                    fun.toPythonString());
            initInd++;
        }
    }

    private static void writeToFile(List<NewtonData> iterationData, List<Double> alphas, String fileName, String function) {
        Path file = Path.of("pythonGraphics/input/").resolve(fileName);
        try (var writer = new PrintWriter(Files.newBufferedWriter(file))) {
            writer.println(function);

            alphas.forEach(a -> writer.print(a + " "));
            writer.println();

            iterationData.forEach(it -> writer.println(it.getX().get(0) + " " + it.getX().get(1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateForAllNewton(String genID, Function fun, List<Vector> initialPoints) {
        generate(genID, "classic", fun, initialPoints);
        generate(genID, "steepest", fun, initialPoints);
        generate(genID, "direction", fun, initialPoints);
    }

    public static void main(String[] args) {
        generateForAllNewton("F2", Functions.f2, List.of(
                new Vector(4, 1)
        ));
    }
}
