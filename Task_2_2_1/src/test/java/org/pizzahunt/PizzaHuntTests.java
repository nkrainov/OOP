package org.pizzahunt;

import java.io.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pizzahunt.exceptions.InvalidFormatException;

import static java.lang.Thread.sleep;

/**
 * Тесты.
 */
public class PizzaHuntTests {

    /**
     * Просто тест, что оно работает без исключений.
     */
    @Test
    void simpleTest() throws IOException, InterruptedException {
        PizzaHunt pizzahunt = new PizzaHunt(new File("src"
                + File.separator
                + "test"
                + File.separator
                + "resources"
                + File.separator
                + "simpleTestConfiguration.json"), OutputStream.nullOutputStream());

        pizzahunt.startWorkDay();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();

        sleep(10000);
    }

    /**
     * Если нам дали JSON с неверно обозначенными пекарями или курьерами, ты мы должны рухнуть.
     */
    @Test
    void incorrectWorkersJsonTest() throws IOException {
        try {
            PizzaHunt pizzahunt = new PizzaHunt(new File("src"
                    + File.separator
                    + "test"
                    + File.separator
                    + "resources"
                    + File.separator
                    + "incorrectBakersJSONTest.json"), System.out);
            Assertions.fail();
        } catch (InvalidFormatException e) {
            System.out.println(e.getMessage());
        }

        try {
            PizzaHunt pizzahunt = new PizzaHunt(new File("src"
                    + File.separator
                    + "test"
                    + File.separator
                    + "resources"
                    + File.separator
                    + "incorrectCouriersJSONTest.json"), System.out);
            Assertions.fail();
        } catch (InvalidFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Если нам дали json с неправильными параметрами пиццерии, то мы должны получить исключение.
     */
    @Test
    void incorrectPizzaHuntJsonTest() throws IOException {
        try {
            PizzaHunt pizzahunt = new PizzaHunt(new File("src"
                    + File.separator
                    + "test"
                    + File.separator
                    + "resources"
                    + File.separator
                    + "incorrectPizzaHuntJSONTest.json"), System.out);
            Assertions.fail();
        } catch (InvalidFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Тест закрытия пиццерии.
     */
    @Test
    void closePizzaHuntTest() throws IOException, InterruptedException {
        PizzaHunt pizzahunt = new PizzaHunt(new File("src"
                + File.separator
                + "test"
                + File.separator
                + "resources"
                + File.separator
                + "simpleTestConfiguration.json"), System.out);

        pizzahunt.startWorkDay();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.closePizzaHunt();
    }

    static class MyOutputStream extends OutputStream {
        boolean[] arr = new boolean[5];

        @Override
        public void write(int b) throws IOException {
            return;
        }

        @Override
        public void write(byte[] b) throws IOException {
            String string = new String(b);
            String[] str = string.split(" ");
            boolean flag = true;
            for (String s : str) {
                if (s.equals("courier")) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                return;
            }

            if (str[str.length-3].equals("delivered")
                    && str[str.length-2].equals("pizza")) {
                arr[Integer.parseInt(str[str.length-1].trim())] = true;
            }
        }
    }

    @Test
    void allPizzaOnTheTargetTest() throws IOException, InterruptedException {
        MyOutputStream MyOutputStreamWriter = new MyOutputStream();
        PizzaHunt pizzahunt = new PizzaHunt(new File("src"
                + File.separator
                + "test"
                + File.separator
                + "resources"
                + File.separator
                + "simpleTestConfiguration.json"), MyOutputStreamWriter);

        pizzahunt.startWorkDay();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.makeOrder();
        pizzahunt.closePizzaHunt();

        for (int i = 0; i < 5; i++) {
            Assertions.assertTrue(MyOutputStreamWriter.arr[i]);
        }
    }
}
