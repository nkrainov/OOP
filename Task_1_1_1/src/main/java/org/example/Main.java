package org.example;

public class Main {
    public static void main(String[] args) {
        int[] ans = HeapSort.sort(new int[] {5, 4, 3, 2, 8, 234, 5555, -134,4, 1});
        for (int i = 0; i< ans.length; i++){
            System.out.println(ans[i]);
        }
    }
}
