package org.example;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Тестирование классов.
 */
public class Tests {
    @Test void check() {
        int[] firstTest = HeapSort.sort(new int[] {4, 3, 2, 1, 0});
        int[] firstAnswer = {0, 1, 2, 3, 4};

        assert Arrays.equals(firstTest, firstAnswer);

        int[] secondTest = HeapSort.sort(new int[] {0});
        int[] secondAnswer = {0};

        assert Arrays.equals(firstTest, firstAnswer);

        int[] thirdTest = HeapSort.sort(new int[0]);
        int[] thirdAnswer = new int[0];

        assert Arrays.equals(thirdTest, thirdAnswer);

        int[] fourTest = HeapSort.sort(new int[] {2345, -23213, 4434, 2322});
        int[] fourAnswer = {-23213, 2322, 2345, 4434};

        assert Arrays.equals(fourTest, fourAnswer);

        int[] fiveTest = HeapSort.sort(new int[] {-1123, 2, 1123});
        int[] fiveAnswer = {-1123, 2, 1123};

        assert Arrays.equals(fiveTest, fiveAnswer);

        int[] sixTest = HeapSort.sort(new int[] {8, 8, 8, 7, 7, 7, 7});
        int[] sixAnswer = {7, 7, 7, 7, 8, 8, 8};

        assert Arrays.equals(sixTest, sixAnswer);


    }
}
