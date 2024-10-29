package org.example;

import org.hashtable.HashTable;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        HashTable<Integer, String> h = new HashTable<>();
        h.put(1, "old");
        h.put(2, "new");
        h.put(3, "irtegov");
        h.put(4, "old");
        h.put(5, "new");
        h.put(6, "irtegov");
        h.put(7, "old");
        h.put(8, "new");
        h.put(9, "irtegov");
        h.put(10, "old");
        h.put(21, "new");
        h.put(31, "irtegov");
        h.put(13, "old");
        h.put(24, "new");
        h.put(35, "irtegov");
        h.put(16, "old");
        h.put(27, "new");
        h.put(38, "irtegov");
        h.put(19, "old");
        h.put(299, "new");
        h.put(37, "irtegov");
        h.put(15, "old");
        h.put(265, "new");
        h.put(3666, "irtegov");
        h.put(1234, "old");
        h.put(24342, "new");
        h.put(3111, "irtegov");
        Iterator<HashTable.Pair<Integer, String>> f = h.getIterator();
        while (f.hasNext()) {
            System.out.println(f.next());
        }
    }
}