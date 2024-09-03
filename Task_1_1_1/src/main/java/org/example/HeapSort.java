package org.example;

public class HeapSort {
    public static int[] sort(int[] array) {
        int[] helpArr = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            helpArr[i] = array[i];
            siftUp(helpArr, i);
        }


        int[] answer = new int[array.length];
        for (int i = answer.length - 1; i >= 0; i--){
            answer[answer.length - i - 1] = extractMin(helpArr, i);
        }

        return answer;

    }

    private static int extractMin(int[] arr, int last_index){
        int ans = arr[0];
        arr[0] = arr[last_index];
        siftdown(0, arr, last_index);
        return ans;
    }

    private static void siftdown(int index, int[] arr, int last_index){
        if (last_index>= 2 * index + 1 && last_index >= 2 * index + 2) {
            int help,left_child = 2*index + 1, right_child = 2*index + 2;
            if (arr[index] > Math.min(arr[left_child], arr[right_child])) {
                help = arr[index];
                if (Math.min(arr[left_child], arr[right_child]) == arr[left_child]) {
                    arr[index] = arr[left_child];
                    arr[left_child] = help;
                    siftdown(left_child, arr, last_index);
                }
                else {
                    arr[index] = arr[right_child];
                    arr[right_child] = help;
                    siftdown(right_child, arr, last_index);
                }
            }
        }
        else if (last_index >= 2 * index + 1) {
            int help, left_child = 2 * index + 1;
            if (arr[index] > arr[left_child]) {
                help = arr[index];
                arr[index] = arr[left_child];
                arr[left_child] = help;
            }
        }
    }

    private static void siftUp(int[] array, int index){
        if (index > 0) {
            int help, parent = (index - 1) / 2;
            if (array[parent] > array[index]){
                help = array[index];
                array[index] = array[parent];
                array[parent] = help;

                siftUp(array, parent);
            }
        }
    }
}
