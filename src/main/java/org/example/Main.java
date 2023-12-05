package org.example;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        int N = 12;
        int[][] matrix = new int[N][N];

        int[][] matrix2 = {
                {99, 2, 3},
                {99, 99, 3},
                {24, 45, 66},

        };

        // Generate matrix A
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = rand.nextInt(10);
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

        int det = forkJoinPool.invoke(task);
        forkJoinPool.shutdown();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;

        // Выводим результат
        System.out.println("Determinant of the matrix is: " + det);
        System.out.println("Execution time with multithreading: " + duration + " ms");



    }
}
