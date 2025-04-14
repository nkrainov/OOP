package org.hashtable;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * Класс, реализующий хэш-таблицу.
 */
public class HashTable<K, V> implements Iterable<HashTable.Pair<K, V>> {
    /**
     * Класс, реализующий элемент хеш-таблицы, хранящий пару
     * ключ-значение.
     */
    public static class Pair<K, V> {
        final K key;
        V value;
        int hashcode;
        boolean alive;


        /**
         * Конструктор.
         */
        Pair(K first, V second, int hashcode) {
            this.key = first;
            this.value = second;
            this.hashcode = hashcode;
            this.alive = true;
        }

        /**
         * Преобразование в строковое представление.
         */
        @Override
        public String toString() {
            return "key: " + key + ", value: " + value + ".\n";
        }

        /**
         * Получение ключа.
         */
        public K getKey() {
            return key;
        }

        /**
         * Получение значения.
         */
        public V getValue() {
            return value;
        }
    }

    private Pair<?, ?>[] table;
    private int capacity = 16;
    private int curSize = 0;
    int modificationCount = 0;

    /**
     * Конструктор.
     */
    public HashTable() {
        table = new Pair<?, ?>[capacity];
    }

    /**
     * Метод, кладущий в таблицу пару ключ-значение.
     *
     * @return Возвращает предыдущее значение по этому ключу.
     */
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key or value is null");
        }

        if (curSize >= 0.75 * capacity) {
            modificationCount++;
            resize();
        }

        modificationCount++;
        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        while (!(table[index] == null)) {
            if (table[index].hashcode == key.hashCode() && table[index].key.equals(key)
                    && table[index].alive) {
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

    /**
     * Метод, удаляющий из таблицы пару ключ-значение.
     *
     * @return Возвращает значение по этому ключу.
     */
    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }

        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        while (!(table[index] == null)) {
            if (table[index].alive
                    && table[index].hashcode == key.hashCode()
                    && table[index].key.equals(key)) {
                Pair<K, V> pair = (Pair<K, V>) table[index];
                V ans = pair.value;
                pair.alive = false;
                modificationCount++;
                return ans;
            }
            index = (index + 1) % table.length;
        }

        return null;
    }


    /**
     * Метод, позволяющий получить значение по этому ключу.
     *
     * @return Возвращает значение по этому ключу.
     */
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        while (!(table[index] == null)) {
            if (table[index].hashcode == key.hashCode()
                    && table[index].key.equals(key)
                    && table[index].alive) {
                return (V) table[index].value;
            }
            index = (index + 1) % table.length;
        }
        return null;
    }

    /**
     * Метод, обновляющий в таблице пару ключ-значение.
     *
     * @return Возвращает true при успешном обновлении, иначе false.
     */
    public boolean update(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key or value is null");
        }

        if (curSize >= 0.75 * capacity) {
            modificationCount++;
            resize();
        }

        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        while (!(table[index] == null)) {
            if (table[index].hashcode == key.hashCode() && table[index].key.equals(key) &&
                    table[index].alive) {
                Pair<K, V> pair = (Pair<K, V>) table[index];
                pair.value = value;
                modificationCount++;
                return true;
            }
            index = (index + 1) % table.length;
        }

        return false;
    }

    /**
     * Проверяет наличие пары с данным ключом.
     *
     * @return Если пара с таким ключом есть, то возвращает true
     * иначе false.
     */
    public boolean search(K key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }

        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        while (!(table[index] == null)) {
            if (table[index].hashcode == key.hashCode() && table[index].key.equals(key) &&
                    table[index].alive) {
                return true;
            }
            index = (index + 1) % table.length;
        }

        return false;
    }

    /**
     * Преобразование в строковое представление.
     */
    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].alive) {
                ans.append(table[i].toString());
            }
        }
        return ans.toString();
    }

    /**
     * Метод, увеличивающий таблицу в два раза.
     */
    private void resize() {
        Pair<?, ?>[] newTable = new Pair[capacity * 2];
        int newSize = 0;
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].alive) {
                newSize++;
                int index = (table[i].key.hashCode() & 0x7FFFFFFF) % (capacity * 2);
                while (!(newTable[index] == null)) {
                    index = (index + 1) % (capacity * 2);
                }

                newTable[index] = new Pair<>(table[i].key, table[i].value, table[i].key.hashCode());

            }
        }

        curSize = newSize;
        capacity *= 2;
        table = newTable;

    }

    /**
     * Проверка на равенство таблиц. Не симметричен!
     */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof HashTable<?, ?>)) {
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

    /**
     * Возвращает итератор по этой таблице.
     */

    @Override
    public Iterator<Pair<K, V>> iterator() {
        return new MyIterator<>(this);
    }

    /**
     * Реализация итератора для этой таблицы.
     */
    public class MyIterator<T> implements Iterator<T> {
        private int curIndex = 0;
        private int curIndexSearch = 0;
        private final int memModificationCount;

        /**
         * Конструктор.
         */
        public MyIterator(HashTable<K, V> tab) {
            this.memModificationCount = modificationCount;
        }

        /**
         * Проверка наличия следующего элемента.
         */
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

        /**
         * Возвращает следующий элемент.
         */
        @Override
        public T next() {
            if (memModificationCount != modificationCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                return null;
            }
            curIndex = curIndexSearch;
            return (T) table[curIndexSearch++];
        }

        /**
         * Удаление элемента из таблицы.
         */
        @Override
        public void remove() {
            if (memModificationCount != modificationCount) {
                throw new ConcurrentModificationException();
            }
            if (table[curIndex] == null) {
                throw new NullPointerException();
            }

            table[curIndex].alive = false;
        }

    }
}
