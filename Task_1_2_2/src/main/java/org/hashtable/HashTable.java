package org.hashtable;


import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class HashTable<K, V> {
    public static class Pair<K, V> {
        final K key;
        V value;
        int hashcode;
        boolean alive;

        Pair(K first, V second, int hashcode) {
            this.key = first;
            this.value = second;
            this.hashcode = hashcode;
            this.alive = true;
        }

        @Override
        public String toString() {
            return "key " + key + ", value " + value + ".";
        }
    }
    private Pair<?, ?>[] table;
    private int capacity = 16;
    private int curSize = 0;
    int modificationCount = 0;
    public HashTable() {
        table = new Pair<?, ?>[capacity];
    }

    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key or value is null");
        }
        modificationCount++;
        if (curSize >= 0.75 * capacity) {
            resize();
        }

        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        while (!(table[index] == null)) {
            if (table[index].hashcode == key.hashCode() && table[index].key.equals(key) &&
            table[index].alive) {
                Pair<K, V> pair = (Pair<K, V>) table[index];
                V ans = pair.value;
                pair.value = value;
                return ans;
            }
            index = (index + 1) % table.length;
        }

        table[index] = new Pair<>(key, value, key.hashCode());
        curSize++;
        return null;
    }

    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }

        modificationCount++;
        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        while (!(table[index] == null)) {
            if (table[index].hashcode == key.hashCode() && table[index].key.equals(key)) {
                Pair<K, V> pair = (Pair<K, V>) table[index];
                V ans = pair.value;
                pair.alive = false;
                return ans;
            }
            index = (index + 1) % table.length;
        }

        table[index].alive = false;
        return null;
    }

    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        while (!(table[index] == null)) {
            if (table[index].hashcode == key.hashCode() && table[index].key.equals(key) &&
                    table[index].alive) {
                return (V) table[index].value;
            }
            index = (index + 1) % table.length;
        }
        return null;
    }

    public boolean containsValue(V value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }

        for (int i = 0; i < capacity; i++) {
            if (table[i].value.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public void update(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key or value is null");
        }
        modificationCount++;
        if (curSize >= 0.75 * capacity) {
            resize();
        }

        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        while (!(table[index] == null)) {
            if (table[index].hashcode == key.hashCode() && table[index].key.equals(key) &&
                    table[index].alive) {
                Pair<K, V> pair = (Pair<K, V>) table[index];
                pair.value = value;
                return;
            }
            index = (index + 1) % table.length;
        }
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].alive) {
                ans.append("key: ").append(table[i].key.toString()).append(" value: ").append(table[i].value.toString()).append("\n");
            }
        }
        return ans.toString();
    }

    private void resize() {
        Pair<?, ?>[] newTable = new Pair[capacity*2];
        int newSize = 0;
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].alive) {
                newSize++;
                int index = (table[i].key.hashCode() & 0x7FFFFFFF) % capacity*2;
                while (!(newTable[index] == null)) {
                    index = (index + 1) % (capacity*2);
                }

                newTable[index] = new Pair<>(table[i].key, table[i].value, table[i].key.hashCode());

            }
        }

        curSize = newSize;
        capacity *= 2;
        table = newTable;

    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof HashTable<?,?>)) {
            return false;
        }

        HashTable<K, V> t = (HashTable<K, V>) o;
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].alive) {
                V value = t.get((K) table[i].key);
                if (value == null || !value.equals(table[i].value)) {
                    return false;
                }
            }
        }
        return true;

    }

    public MyIterator<Pair<K, V>> getIterator() {
        return new MyIterator<>(this);
    }

    public class MyIterator<T> implements Iterator<T>{
        private int curIndex = 0;
        private int curIndexSearch = 0;
        private final int memModificationCount;

        public MyIterator (HashTable<K, V> tab) {
            this.memModificationCount = modificationCount;
        }

        @Override
        public boolean hasNext() {
            if (memModificationCount != modificationCount) {
                throw new ConcurrentModificationException();
            }

            for (int i = curIndexSearch; i < capacity; i++) {
                if (table[i] != null && table[i].alive) {
                    return true;
                }
                curIndexSearch++;
            }

            return false;
        }

        @Override
        public T next() {
            if (memModificationCount != modificationCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                return null;
            }
            curIndex = curIndexSearch;
            return (T)table[curIndexSearch++];
        }

        @Override
        public void remove() {
            if (memModificationCount != modificationCount) {
                throw new ConcurrentModificationException();
            }
            if (table[curIndex] == null) {
                throw new NullPointerException();
            }

            table[curIndex] = null;
        }

    }
}
