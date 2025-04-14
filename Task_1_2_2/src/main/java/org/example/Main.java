package org.example;

import org.hashtable.HashTable;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        HashTable<Integer, String> h = new HashTable<>();
        h.put(1, "old");
        h.put(2, "new");
        Iterator<HashTable.Pair<Integer, String>> f = h.iterator();
        while (f.hasNext()) {
            System.out.println(f.next());
        }
    }
}