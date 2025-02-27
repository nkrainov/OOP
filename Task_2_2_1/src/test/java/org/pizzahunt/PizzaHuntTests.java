package org.pizzahunt;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
    void incorrectWorkersJSONTest() throws IOException {
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
}
