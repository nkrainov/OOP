package org.example;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import java.util.Random;

/**
 * Тестирование классов.
 */
public class Tests {
    @Test void firstCheck() {
        int[] firstTest = HeapSort.sort(new int[] {4, 3, 2, 1, 0});
        int[] firstAnswer = {0, 1, 2, 3, 4};

        assert Arrays.equals(firstTest, firstAnswer);
    }

    @Test void secondCheck() {
        int[] secondTest = HeapSort.sort(new int[] {0});
        int[] secondAnswer = {0};

        assert Arrays.equals(secondTest, secondAnswer);
    }

    @Test void thirdCheck() {
        int[] thirdTest = HeapSort.sort(new int[0]);
        int[] thirdAnswer = new int[0];

        assert Arrays.equals(thirdTest, thirdAnswer);
    }

    @Test void fourCheck() {
        int[] fourTest = HeapSort.sort(new int[] {2345, -23213, 4434, 2322});
        int[] fourAnswer = {-23213, 2322, 2345, 4434};

        assert Arrays.equals(fourTest, fourAnswer);
    }

    @Test void fiveCheck() {
        int[] fiveTest = HeapSort.sort(new int[] {-1123, 2, 1123});
        int[] fiveAnswer = {-1123, 2, 1123};

        assert Arrays.equals(fiveTest, fiveAnswer);
    }

    @Test void sixCheck() {
        int[] sixTest = HeapSort.sort(new int[] {8, 8, 8, 7, 7, 7, 7});
        int[] sixAnswer = {7, 7, 7, 7, 8, 8, 8};

        assert Arrays.equals(sixTest, sixAnswer);
    }

    @Test void checkComplexity() {

        Random rand = new Random();
        int n1 = 1000;
        int n2 = 100000;
        int n3 = 10000000;
        int[] firstArray = new int[n1];
        for (int i = 0; i < n1; i++) {
            firstArray[i] = rand.nextInt(1000000);
        }

        int[] secondArray = new int[n2];
        for (int i = 0; i < n2; i++) {
            secondArray[i] = rand.nextInt(1000000);
        }

        int[] thirdArray = new int[n3];
        for (int i = 0; i < n3; i++) {
            thirdArray[i] = rand.nextInt(1000000);
        }

        long start = System.currentTimeMillis();
        HeapSort.sort(firstArray);
        long finish = System.currentTimeMillis();
        long duration1 = finish - start;
        System.out.println("Массив из 1000 элементов, прошло времени, мс: " + duration1);

        start = System.currentTimeMillis();
        HeapSort.sort(secondArray);
        finish = System.currentTimeMillis();
        long duration2 = finish - start;
        System.out.println("Массив из 100000 элементов, прошло времени, мс: " + duration2);

        start = System.currentTimeMillis();
        HeapSort.sort(thirdArray);
        finish = System.currentTimeMillis();
        long duration3 = finish - start;
        System.out.println("Массив из 10000000 элементов, прошло времени, мс: " + duration3);

        double epsilon = 0.1;
        boolean isEqual1 = Math.abs((Math.log(n1)*n1)/(Math.log(n2)*n2) - duration1/duration2) <= epsilon;
        boolean isEqual2 = Math.abs((Math.log(n2)*n2)/(Math.log(n3)*n3) - duration2/duration3) <= epsilon;
        assert isEqual2 && isEqual1;
    }
}
