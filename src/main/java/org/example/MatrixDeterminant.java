package org.example;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MatrixDeterminant {

    private static final Map<String, Integer> cache = new ConcurrentHashMap<>();
    // Функция для вычисления определителя матрицы
    public int determinant(int[][] matrix) {
        String key = Arrays.deepToString(matrix);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        int n = matrix.length;
        // Базовый случай: если матрица размером 1x1, возвращаем этот элемент
        if (n == 1) {
            return matrix[0][0];
        }

        int det = 0;
        // Рекурсивно вычисляем определитель для каждого элемента первой строки
        for (int i = 0; i < n; i++) {
            // Вычисляем минор для текущего элемента
            int[][] minor = getMinor(matrix, 0, i);
            // Рекурсивно вычисляем определитель минора и обновляем значение определителя матрицы
            det += Math.pow(-1, i) * matrix[0][i] * determinant(minor);
        }

        cache.put(key, det);
        return det;
    }

    // Функция для получения минора матрицы без указанной строки и столбца
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