package org.simply.connected.application.optimization.methods.slae;

import org.simply.connected.application.optimization.methods.slae.generator.Utils;
import org.simply.connected.application.optimization.methods.slae.math.SquareMatrix;
import org.simply.connected.application.optimization.methods.slae.math.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public abstract class AbstractSlaeSolver implements SlaeSolver {

    public abstract Vector solve(SquareMatrix coefficients, Vector freeValues);

    @Override
    public Vector solveFile(File input, File output) throws IOException {
        if (!input.isFile()) {
            throw new IllegalArgumentException("Input is not an existing file");
        }
        try (BufferedReader reader = Files.newBufferedReader(input.toPath(), StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(Files.newBufferedWriter(output.toPath(), StandardCharsets.UTF_8))
        ) {
            int arity = Utils.readInts(reader)[0];
            SquareMatrix coefficients = readMatrix(reader, arity);
            Vector freeValues = Vector.readFrom(reader);
            Vector result = solve(coefficients, freeValues);
            writer.println(Utils.joinToString(result.getData()));
            return result;
        }
    }

    protected abstract SquareMatrix readMatrix(BufferedReader reader, int arity) throws IOException;
}
