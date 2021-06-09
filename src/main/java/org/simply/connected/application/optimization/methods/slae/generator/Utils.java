package org.simply.connected.application.optimization.methods.slae.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static double sum(double[] arr) {
        return Arrays.stream(arr).sum();
    }

    public static int sum(int[] arr) {
        return Arrays.stream(arr).sum();
    }

    public static String joinToString(double[] arr) {
        return Arrays.stream(arr).mapToObj(String::valueOf).collect(Collectors.joining(" "));
    }

    public static String joinToString(int[] arr) {
        return Arrays.stream(arr).mapToObj(String::valueOf).collect(Collectors.joining(" "));
    }

    public static double[] readDoubles(BufferedReader reader) throws IOException {
        return readLine(reader).mapToDouble(Double::parseDouble).toArray();
    }

    public static int[] readInts(BufferedReader reader) throws IOException {
        return readLine(reader).mapToInt(Integer::parseInt).toArray();
    }
    private static Stream<String> readLine(BufferedReader reader) throws IOException {
        return Arrays.stream(reader.readLine().split(" "));
    }

}
