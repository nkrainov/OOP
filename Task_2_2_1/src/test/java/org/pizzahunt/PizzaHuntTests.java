package org.pizzahunt;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * Тесты.
 */
public class PizzaHuntTests {

    /**
     * Простой тест.
     */
    @Test
    void simpleTest() throws IOException, InterruptedException {
        PizzaHunt r = new PizzaHunt(new File("src" +
                File.separator +
                "test" +
                File.separator +
                "resources" +
                File.separator +
                "simpleTestConfiguration"), System.out);

        r.startWorkDay();
        r.makeOrder();
        r.makeOrder();
        r.makeOrder();
        r.makeOrder();
        r.makeOrder();

        sleep(10000);
    }
}
