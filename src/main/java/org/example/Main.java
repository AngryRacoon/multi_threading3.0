package org.example;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        int N = 15;
        long[][] matrix = new long[N][N];

        long[][] matrix2 = {
                {99, 2, 3},
                {99, 99, 3},
                {24, 45, 66},

        };
        long[][] matrix3 = {
                {99, 2, 3,1,75,30},
                {99, 99, 3,12,56,77},
                {24, 45, 66,68,9,2},
                {24, 45, 75,4,64,37},
                {24, 45, 75,4,45,45},
                {24, 34, 75,4,23,13},

        };

        // Generate matrix A
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = rand.nextLong(10);
            }
        }

        long startTime= System.currentTimeMillis(); // Start time
        MatrixDeterminant determinant = new MatrixDeterminant();
        long det2 = determinant.determinant(matrix);
        long endTime= System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Determinant of the matrix is: " + det2);
        System.out.println("Execution time without multithreading: " + duration + " ms");


        startTime = System.currentTimeMillis();
        // Создаем ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // Создаем RecursiveTask для определения матрицы
        ParallelMatrixDeterminant task = new ParallelMatrixDeterminant(matrix);

        // Вызываем ForkJoinPool, чтобы выполнить задачу

        long det = forkJoinPool.invoke(task);
        forkJoinPool.shutdown();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;

        // Выводим результат
        System.out.println("Determinant of the matrix is: " + det);
        System.out.println("Execution time with multithreading: " + duration + " ms");



    }
}
