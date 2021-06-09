package org.simply.connected.application.optimization.methods.slae.util;

import org.simply.connected.application.optimization.methods.slae.math.SquareMatrix;
import org.simply.connected.application.optimization.methods.slae.generator.*;
import org.simply.connected.application.optimization.methods.slae.math.ThinMatrix;
import org.simply.connected.application.optimization.methods.slae.math.DenseMatrix;
import org.simply.connected.application.optimization.methods.slae.math.Vector;
import org.simply.connected.application.optimization.methods.slae.ConjugateGradientSlaeSolver;
import org.simply.connected.application.optimization.methods.slae.GaussSlaeSolver;
import org.simply.connected.application.optimization.methods.slae.LUDecompositionSlaeSolver;
import org.simply.connected.application.optimization.methods.slae.SlaeSolver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import static org.simply.connected.application.optimization.methods.slae.math.Math.*;


public class SlaeTableResearchConstructor {
    public static final String researchDir =
            "C:\\Users\\okare\\IdeaProjects\\OptMethodsLab3\\Research";
    public static final String researchFilePrefix = "_RESULT";

    private static String researchCSV(SlaeSolver solver,
                                      Generator generator,
                                      StringBuilder sb,
                                      String solverID,
                                      String generationID) throws IOException {
        File dir = getOutputDir(solverID, generationID);
        generator.generate(dir, generationID);
        File tempFile = File.createTempFile(generationID, "temp");
        Files.newDirectoryStream(dir.toPath()).forEach(path ->
                {
                    File file = path.toFile();
                    int[] nk = parseFileName(file);
                    int arity = nk[0];
                    Vector sol = AbstractSlaeGenerator.getSolution(arity);
                    Vector ans = null;
                    try {
                        ans = solver.solveFile(file, tempFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    double margin = norm(subtract(sol, ans));
                    String info = Arrays.stream(nk).mapToObj(String::valueOf).collect(Collectors.joining(","));
                    sb.append(String.format("%s,%.20f,%.20f%n", info, margin, margin / norm(sol)));
                }
        );
        writeResearch(sb, generationID, dir);
        return sb.toString();
    }

    private static File getOutputDir(String solverID, String generationID) {
        File dir = Path.of(researchDir).resolve(solverID).resolve(generationID).toFile();
        boolean made = dir.mkdirs();
        return dir;
    }

    private static void writeResearch(StringBuilder sb, String generationID, File dir) throws IOException {
        Path res = dir.toPath().getParent().resolve(generationID + researchFilePrefix);
        try (BufferedWriter writer = Files.newBufferedWriter(res)) {
            writer.write(sb.toString());
        }
    }

    private static String researchConjugateCSV(Generator generator,
                                               StringBuilder sb,
                                               String generationID,
                                               BiFunction<BufferedReader, Integer, SquareMatrix> matrixReader) throws IOException {
        ConjugateGradientSlaeSolver solver = new ConjugateGradientSlaeSolver();
        File dir = getOutputDir("Conjugate", generationID);
        generator.generate(dir, generationID);
        Files.newDirectoryStream(dir.toPath()).forEach(path ->
                {
                    File file = path.toFile();
                    int[] nk = parseFileName(file);
                    try(BufferedReader reader = Files.newBufferedReader(path)) {
                        Vector sol = AbstractSlaeGenerator.getSolution(nk[0]);
                        int arity = Utils.readInts(reader)[0];
                        SquareMatrix matrix = matrixReader.apply(reader, arity);
                        Vector freeValues = Vector.readFrom(reader);
                        Vector ans = solver.solve(matrix, freeValues);
                        int iterations = solver.getIterations();
                        double margin = norm(subtract(sol, ans));
                        String info = Arrays.stream(nk).mapToObj(String::valueOf).collect(Collectors.joining(","));
                        Vector Ax = product(matrix, ans);
                        sb.append(String.format("%s,%d,%.20f,%.20f,%.20f%n",
                                info,
                                iterations,
                                margin,
                                margin / norm(sol),
                                margin / (norm((subtract(freeValues, Ax))) / norm(freeValues))));
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
        );
        writeResearch(sb, generationID, dir);
        return sb.toString();
    }

    public static String researchLUCondCSV(double maxProfileSizeRatio) throws IOException {
        return researchCSV(new LUDecompositionSlaeSolver(),
                new ProfileMatrixGenerator(maxProfileSizeRatio),
                new StringBuilder("n, k, norm, norm\n"),
                "LU",
                "Cond");
    }

    public static String researchLUHilbertCSV() throws IOException {
        return researchCSV(new LUDecompositionSlaeSolver(),
                new HilbertMatrixGenerator(),
                new StringBuilder("n, norm, norm\n"),
                "LU",
                "Hilbert");
    }

    public static String researchGaussCondCSV() throws IOException {
        return researchCSV(new GaussSlaeSolver(),
                new DenseMatrixGenerator(),
                new StringBuilder("n, k, norm, norm\n"),
                "Gauss",
                "Cond");
    }

    private static String researchConjugate(double maxPortraitSizeRatio, boolean isPositive, String generationID) throws IOException {
        return researchConjugateCSV(
                new ThinMatrixGenerator(maxPortraitSizeRatio, isPositive),
                new StringBuilder("n, iterations, norm, norm, cond\n"),
                generationID,
                (reader, arity) -> {
                    try {
                        return ThinMatrix.readFrom(reader, arity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

    public static String researchConjugateCondCSV(double maxPortraitSizeRatio) throws IOException {
        return researchConjugate(maxPortraitSizeRatio, false, "Cond");
    }

    public static String researchConjugateCondNegativeCSV(double maxPortraitSizeRatio) throws IOException {
        return researchConjugate(maxPortraitSizeRatio, true, "CondNegative");
    }

    public static String researchConjugateHilbertCSV() throws IOException {
        return researchConjugateCSV(
                new HilbertDenseMatrixGenerator(),
                new StringBuilder("n, iterations, norm, norm, cond\n"),
                "Hilbert",
                (reader, arity) -> {
                    try {
                        return DenseMatrix.readFrom(reader, arity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

    private static int[] parseFileName(File file) {
        return Arrays.stream(file.getName().split("_")).skip(1).mapToInt(Integer::parseInt).toArray();
    }
}
