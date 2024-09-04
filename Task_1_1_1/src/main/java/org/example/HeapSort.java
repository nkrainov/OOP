package org.example;

/**
 * Класс, содержащий необходимые функции для пирамидальной сортировки.
 * Главная функция - sort, остальные вспомогательные для главной (и поэтому приватные)
 */
public class HeapSort {

    /**
     * Функция пирамидальной сортировки.
     *
     * @return возвращает отсортированный массив
     */
    public static int[] sort(int[] array) {
        int[] answer = new int[array.length];
        if (array.length > 1) {
            //Вставляем элементы в кучу, запускаем siftUp для сохранения инварианта кучи
            int[] helpArr = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                helpArr[i] = array[i];
                siftUp(helpArr, i);
            }

            for (int i = answer.length - 1; i >= 0; i--) {
                answer[answer.length - i - 1] = extractMin(helpArr, i);
            }

        }
        return answer;

    }

    /**
     * Функция, извлекающее минимальное значение из кучи и затем выполняющее
     * sift_down для сохранения инварианта кучи.
     *
     * @return возвращает корень этой кучи
     */
    private static int extractMin(int[] arr, int lastIndex) {
        int ans = arr[0];
        arr[0] = arr[lastIndex];
        siftdown(0, arr, lastIndex);
        return ans;
    }

    /**
     * Функция, которая опускает элементы ниже по дереву, если они больше какого-либо своего сына.
     * После полного завершения работы функции будет выполняться инвариант кучи
     */
    private static void siftdown(int index, int[] arr, int lastIndex) {
        int leftChild;
        int help;
        if (lastIndex >= 2 * index + 1 && lastIndex >= 2 * index + 2) {
            leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            if (arr[index] > Math.min(arr[leftChild], arr[rightChild])) {
                help = arr[index];
                if (Math.min(arr[leftChild], arr[rightChild]) == arr[leftChild]) {
                    arr[index] = arr[leftChild];
                    arr[leftChild] = help;
                    siftdown(leftChild, arr, lastIndex);
                }
                else {
                    arr[index] = arr[rightChild];
                    arr[rightChild] = help;
                    siftdown(rightChild, arr, lastIndex);
                }
            }
        } else if (lastIndex >= 2 * index + 1) {
            leftChild = 2 * index + 1;
            if (arr[index] > arr[leftChild]) {
                help = arr[index];
                arr[index] = arr[leftChild];
                arr[leftChild] = help;
            }
        }
    }


    /**
     * Функция, которая поднимает элементы вверх по дереву, если они меньше своего родителя.
     * После полного завершения работы функции будет выполняться инвариант кучи
     */
    private static void siftUp(int[] array, int index) {
        if (index > 0) {
            int help;
            int parent = (index - 1) / 2;
            if (array[parent] > array[index]) {
                help = array[index];
                array[index] = array[parent];
                array[parent] = help;

                siftUp(array, parent);
            }
        }
    }
}
