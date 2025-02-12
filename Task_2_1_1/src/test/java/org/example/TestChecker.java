package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Scanner;

/**
    Тестирование решения задачи.
 */
public class TestChecker {
    //int[] testExample = getTestExample();

    /**
        Загрузка из файла массива из всех простых чисел до 500_000_000 для теста времени.
     */
    int[] getTestExample() {
        String path = System.getProperty("user.dir") +
                File.separator
                + "src"
                + File.separator
                + "test"
                + File.separator
                + "resources"
                + File.separator
                + "primeNumbers.txt";
        int count = 0;
        int index = 0;
        int[] arr = null;

        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (count == 0) {
                    count = Integer.parseInt(line);
                    arr = new int[count];
                } else {
                    arr[index] = Integer.parseInt(line);
                    index++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return arr;
    }

    /**
        Проверка корректности последовательного нахождения составного числа в массиве.
     */
    @Test
    void testSequentialChecker() {
        int[] numbers = {6, 8, 7, 13, 5, 9, 4};
        Assertions.assertTrue(SequentialChecker.checkCompositeNumbers(numbers));

        int[] number = {20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
                6998009, 6998029, 6998039, 20165149, 6998051, 6998053};
        Assertions.assertFalse(SequentialChecker.checkCompositeNumbers(number));

    }

    /**
        Тестирование параллельного решения задачи.
     */
    @Test
    void testThreadChecker() {
        int[] number = {6, 8, 7, 13, 5, 9, 4};
        ThreadChecker.setCountOfWorkers(6);
        Assertions.assertTrue(ThreadChecker.checkCompositeNumbers(number));
        int[] numbers = {20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
                6998009, 6998029, 6998039, 20165149, 6998051, 6998053};
        Assertions.assertFalse(ThreadChecker.checkCompositeNumbers(numbers));
    }

    /**
        Тестирование решения задачи с помощью parallelStream.
     */
    @Test
    void testParallelStreamChecker() {
        int[] number = {6, 8, 7, 13, 5, 9, 4};
        Assertions.assertTrue(ParallelStreamChecker.checkCompositeNumbers(number));
        int[] numbers = {20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
                6998009, 6998029, 6998039, 20165149, 6998051, 6998053};
        Assertions.assertFalse(ParallelStreamChecker.checkCompositeNumbers(numbers));
    }

//    /***
//     Проверка времени работы последовательного решения на большом примере.
//     */
//    @Test
//    void checkSeqCheckWorkTime() {
//        double start = System.currentTimeMillis();
//        Assertions.assertFalse(SequentialChecker.checkCompositeNumbers(testExample));
//        double end = System.currentTimeMillis();
//        System.out.println("Time taken: " + (end - start) / 1000 + " seconds");
//    }
//
//    /***
//     Проверка времени работы параллельного решения на большом примере.
//     */
//    @Test
//    void checkThreadCheckWorkTime() {
//        System.out.println(testExample.length);
//        ThreadChecker.setCountOfWorkers(5);
//        double start = System.currentTimeMillis();
//        Assertions.assertFalse(ThreadChecker.checkCompositeNumbers(testExample));
//        double end = System.currentTimeMillis();
//        System.out.println("Time taken by threads: " + (end - start) / 1000 + " seconds");
//    }
//
//    /***
//     Проверка времени работы решения через parallelStream на большом примере.
//     */
//    //@Test
//    void checkParallelStreamCheckWorkTime() {
//        double start = System.currentTimeMillis();
//        Assertions.assertFalse(ParallelStreamChecker.checkCompositeNumbers(testExample));
//        double end = System.currentTimeMillis();
//        System.out.println("Time taken by parallel stream: " + (end - start) / 1000 + " seconds");
//    }
}
