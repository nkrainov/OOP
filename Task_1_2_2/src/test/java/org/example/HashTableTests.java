package org.example;

import org.hashtable.HashTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * Класс тестов.
 */
public class HashTableTests {

    /**
     * Тест put и remove.
     */
    @Test
    void testPutAndRemove() {
        HashTable<String, String> table = new HashTable<>();
        
        Assertions.assertNull(table.put("test1", "val1"));
        Assertions.assertNull(table.put("test2", "val2"));
        Assertions.assertNull(table.put("test3", "val3"));
        Assertions.assertNull(table.put("test4", "val4"));
        Assertions.assertNull(table.put("test5", "val5"));
        Assertions.assertNull(table.put("test6", "val6"));
        Assertions.assertNull(table.put("test7", "val7"));

        Assertions.assertNull(table.put("test8", "val8"));
        Assertions.assertNull(table.put("test9", "val9"));
        Assertions.assertNull(table.put("test10", "val10"));
        Assertions.assertNull(table.put("test11", "val11"));
        Assertions.assertNull(table.put("test12", "val12"));
        Assertions.assertNull(table.put("test13", "val13"));
        Assertions.assertNull(table.put("test14", "val14"));
        Assertions.assertNull(table.put("test15", "val15"));
        Assertions.assertNull(table.put("test16", "val16"));
        Assertions.assertNull(table.put("test17", "val17"));
        Assertions.assertNull(table.put("test18", "val18"));
        Assertions.assertNull(table.put("test19", "val19"));

        try {
            table.put(null, null);
            Assertions.fail();
        } catch (NullPointerException e) {
            Assertions.assertTrue(true);
        }
        try {
            table.remove(null);
            Assertions.fail();
        } catch (NullPointerException e) {
            Assertions.assertTrue(true);
        }

        Assertions.assertEquals("val1", table.remove("test1"));
        Assertions.assertEquals("val2", table.remove("test2"));
        Assertions.assertEquals("val3", table.remove("test3"));
        Assertions.assertEquals("val4", table.remove("test4"));
        Assertions.assertEquals("val5", table.remove("test5"));
        Assertions.assertEquals("val6", table.remove("test6"));
        Assertions.assertEquals("val7", table.remove("test7"));

        Assertions.assertEquals("val19", table.put("test19", "val191"));

        Assertions.assertNull(table.remove("test1"));
        Assertions.assertNull(table.remove("test2"));
        Assertions.assertNull(table.remove("test3"));
        Assertions.assertNull(table.remove("test4"));
        Assertions.assertNull(table.remove("test5"));
        Assertions.assertNull(table.remove("test6"));
        Assertions.assertNull(table.remove("test7"));
    }

    /**
     * Тест update.
     */
    @Test
    void testUpdate() {
        HashTable<String, String> table = new HashTable<>();
        table.put("test1", "val1");
        table.put("test2", "val2");
        table.put("test3", "val3");
        table.put("test4", "val4");
        table.put("test5", "val5");
        table.put("test6", "val6");
        table.put("test7", "val7");

        try {
            table.update(null, null);
            Assertions.fail();
        } catch (NullPointerException e) {
            Assertions.assertTrue(true);
        }
        try {
            table.get(null);
            Assertions.fail();
        } catch (NullPointerException e) {
            Assertions.assertTrue(true);
        }

        Assertions.assertTrue(table.update("test1", "val11"));
        Assertions.assertTrue(table.update("test2", "val12"));
        Assertions.assertTrue(table.update("test3", "val13"));
        Assertions.assertTrue(table.update("test4", "val14"));
        Assertions.assertTrue(table.update("test5", "val15"));
        Assertions.assertTrue(table.update("test6", "val16"));
        Assertions.assertTrue(table.update("test7", "val17"));

        Assertions.assertEquals("val11", table.get("test1"));
        Assertions.assertEquals("val12", table.get("test2"));
        Assertions.assertEquals("val13", table.get("test3"));
        Assertions.assertEquals("val14", table.get("test4"));
        Assertions.assertEquals("val15", table.get("test5"));
        Assertions.assertEquals("val16", table.get("test6"));
        Assertions.assertEquals("val17", table.get("test7"));
    }

    /**
     * Тест search.
     */
    @Test
    void testSearch() {
        HashTable<String, String> table = new HashTable<>();
        table.put("test1", "val1");
        table.put("test2", "val2");
        table.put("test3", "val3");
        table.put("test4", "val4");
        table.put("test5", "val5");
        table.put("test6", "val6");
        table.put("test7", "val7");

        Assertions.assertTrue(table.search("test1"));
        Assertions.assertTrue(table.search("test2"));
        Assertions.assertTrue(table.search("test3"));
        Assertions.assertTrue(table.search("test4"));
        Assertions.assertTrue(table.search("test5"));
        Assertions.assertTrue(table.search("test6"));
        Assertions.assertTrue(table.search("test7"));
    }

    /**
     * Тест методов итератора.
     */
    @Test
    void testIterator() {
        HashTable<String, String> table = new HashTable<>();
        Assertions.assertNull(table.put("test1", "val1"));
        Assertions.assertNull(table.put("test2", "val2"));
        Assertions.assertNull(table.put("test3", "val3"));
        Iterator<HashTable.Pair<String, String>> iter = table.iterator();
        Assertions.assertTrue(iter.hasNext());
        Assertions.assertNotNull(iter.next());

        try {
            iter.remove();
        } catch (Exception e) {
            Assertions.fail();
        }

        table.put("test4", "val4");
        try {
            iter.next();
            Assertions.fail();
        } catch (ConcurrentModificationException e) {
            Assertions.assertTrue(true);
        }
        try {
            iter.remove();
            Assertions.fail();
        } catch (ConcurrentModificationException e) {
            Assertions.assertTrue(true);
        }
        try {
            iter.hasNext();
            Assertions.fail();
        } catch (ConcurrentModificationException e) {
            Assertions.assertTrue(true);
        }

        for (HashTable.Pair<String, String> s : table) {
            System.out.print(s.toString());
        }
    }

    /**
     * Тест сравнения таблиц.
     */
    @Test
    void testEquals() {
        HashTable<String, String> table = new HashTable<>();

        table.put("test1", "val1");
        table.put("test2", "val2");
        table.put("test3", "val3");
        table.put("test4", "val4");
        table.put("test5", "val5");
        table.put("test6", "val6");
        table.put("test7", "val7");

        HashTable<String, String> table1 = new HashTable<>();

        table1.put("test1", "val1");
        table1.put("test2", "val2");
        table1.put("test3", "val3");
        table1.put("test4", "val4");
        table1.put("test5", "val5");
        table1.put("test6", "val6");
        table1.put("test7", "val7");

        Assertions.assertEquals(table1, table);

        table1.put("test8", "val8");

        Assertions.assertNotEquals(table1, table);
    }
}
