package org.example;

public class Main {
    public static void main(String[] args) {
        int[] array = {2345, 323, 123, 77868, 342, 65823, 568562, -213, -3333, -687};
        System.out.print("Array = ");
        for (int elem : array) {
          System.out.print(elem);
          System.out.print(" ");
        }
        System.out.println(" ");
        int[] sortArray = HeapSort.sort(array);
        System.out.print("Sorted array = ");
        for (int elem : sortArray) {
          System.out.print(elem);
          System.out.print(" ");
        }
    }
}
