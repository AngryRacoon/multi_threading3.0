package org.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class ParallelMatrixDeterminant extends RecursiveTask<Integer> {

    private static final Map<String, Integer> cache = new ConcurrentHashMap<>();
    private int[][] matrix;

    public ParallelMatrixDeterminant(int[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    protected Integer compute() {
        String key = Arrays.deepToString(matrix);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        int n = matrix.length;

        if (n == 1) {
            return matrix[0][0];
        }

        int det = 0;
        RecursiveTask<Integer>[] forks = new RecursiveTask[n];
        for (int i = 0; i < n; i++) {
            int[][] minor = getMinor(matrix, 0, i);
            forks[i] = new ParallelMatrixDeterminant(minor);
            forks[i].fork();
        }
        for (int i = 0; i < n; i++) {
            Integer result = forks[i].join();
            if (result == null) {
                result = 0; // Ensure that we never have a null result
            }
            det += ((int) Math.pow(-1, i)) * matrix[0][i] * result;
        }

        cache.put(key, det);
        return det;
    }

    public static int[][] getMinor(int[][] matrix, int row, int col) {
        int n = matrix.length;
        int[][] minor = new int[n - 1][n - 1];
        int minorRow = 0, minorCol = 0;
        for (int i = 0; i < n; i++) {
            if (i != row) {
                minorCol = 0;
                for (int j = 0; j < n; j++) {
                    if (j != col) {
                        minor[minorRow][minorCol] = matrix[i][j];
                        minorCol++;
                    }
                }
                minorRow++;
            }
        }
        return minor;
    }
}