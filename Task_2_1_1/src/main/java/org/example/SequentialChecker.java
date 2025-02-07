package org.example;

/**
    Однопоточное решение задачи нахождения составного числа в массиве.
 */
public class SequentialChecker {

    /**
        Метод, реализующий это решение.
     */
    public static boolean checkCompositeNumbers(int[] numbers) {
        for (int number : numbers) {
            int sqrt = (int) Math.ceil(Math.sqrt(number));
            for (int i = 2; i <= sqrt; i++) {
                if (number % i == 0) {
                    return true;
                }
            }
        }

        return false;
    }
}
