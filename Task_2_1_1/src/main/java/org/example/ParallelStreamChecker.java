package org.example;

import java.util.stream.*;

/**
    Решение задачи через parallelStream.
 */
public class ParallelStreamChecker {

    /**
        Метод, реализующий решение задачи.
     */
    public static boolean checkCompositeNumbers(int[] numbers) {
        return IntStream.of(numbers).parallel().filter(ParallelStreamChecker::isComposite).findAny().isPresent();
    }

    /**
        Просто проверка на НЕ простоту.
     */
    private static boolean isComposite(int number) {
        int sqrt = (int) Math.sqrt(number);
        for (int i = 2; i < sqrt + 1; i++) {
            if (number % i == 0) {
                return true;
            }
        }

        return false;
    }
}
