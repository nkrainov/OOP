package org.example;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class Main {
    static Object lock = new Object();
    public static void main(String[] args) throws IOException, InterruptedException {
        PizzaHunt r = new PizzaHunt("src\\main\\java\\org\\example\\cong.json");

        r.startWorkDay();
        r.makeOrder();
        r.makeOrder();
        r.makeOrder();
        r.makeOrder();
        r.makeOrder();

        sleep(10000);
        sleep(11);
        r.startWorkDay();
    }
}