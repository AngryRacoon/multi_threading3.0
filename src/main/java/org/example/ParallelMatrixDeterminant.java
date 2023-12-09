package org.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;
import java.util.Arrays;
import java.util.Map;

public class ParallelMatrixDeterminant extends RecursiveTask<Long> {

    private static final Map<String, Long> cache = new ConcurrentHashMap<>();
    private final long[][] matrix;

    public ParallelMatrixDeterminant(long[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    protected Long compute() {
        String key = Arrays.deepToString(matrix);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        int n = matrix.length;

        if (n == 0) {
            return (long)0; // Return 0 for an empty matrix
        } else if (n == 1) {
            return matrix[0][0]; // Return the single element for a 1x1 matrix
        } else if (n == 2) {
            // Return the determinant for a 2x2 matrix directly without further recursion
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        long det = 0;
        RecursiveTask<Long>[] forks = new RecursiveTask[n];
        for (int i = 0; i < n; i++) {
            long[][] minor = getMinor(matrix, 0, i, n);

            forks[i] = new ParallelMatrixDeterminant(minor);
            forks[i].fork();
        }
        for (int i = 0; i < n; i++) {
            Long result = forks[i].join();
/*            if (result == null) {
                result = 0; // Ensure that we never have a null result
            }*/
            det += ((long) Math.pow(-1, i)) * matrix[0][i] * result;
        }

        cache.put(key, det);
        return det;
    }

    public static long[][] getMinor(long[][] matrix, int row, int col, int n) {

        long[][] minor = new long[n - 1][n - 1];
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