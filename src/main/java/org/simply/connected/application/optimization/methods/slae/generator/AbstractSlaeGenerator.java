package org.simply.connected.application.optimization.methods.slae.generator;

import org.simply.connected.application.optimization.methods.slae.math.SquareMatrix;
import org.simply.connected.application.optimization.methods.slae.math.Vector;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

import static org.simply.connected.application.optimization.methods.slae.math.Math.product;


public abstract class AbstractSlaeGenerator implements Generator {

    @Override
    public void generate(File directory, String generationID) throws IOException {
        if (!directory.exists()) {
            directory.mkdirs();
        }
        for (int arity : getArities()) {
            Vector solution = getSolution(arity);
            List<SquareMatrix> matrices = getMatrices(arity);
            for (int matrixInd = 0; matrixInd < matrices.size(); matrixInd++) {
                SquareMatrix matrix = matrices.get(matrixInd);
                Vector freeValues = product(matrix, solution);
                File file = new File(directory, getFileName(arity, matrixInd, generationID));
                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file.toPath()))) {
                    writer.println(arity);
                    writer.println(matrix);
                    writer.println(freeValues);
                }
            }
        }
    }

    protected abstract String getFileName(int arity, int matrixInd, String generationID);
    protected abstract List<SquareMatrix> getMatrices(int arity);
    protected abstract List<Integer> getArities();

    protected static double getRowSum(SquareMatrix matrix, int row) {
        double sum = 0;
        for (int i = 0; i < matrix.getArity(); i++) {
            sum += matrix.get(row, i);
        }
        return sum;
    }

    public static Vector getSolution(int arity) {
        double[] res = new double[arity];
        for (int i = 0; i < arity; i++) {
            res[i] = i + 1;
        }
        return new Vector(res);
    }
}
