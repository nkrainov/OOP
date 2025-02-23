package org.example;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

class Warehouse {
    private final LinkedList<Pizza> queue = new LinkedList<>();
    private final Semaphore semaphorePut;
    private final Semaphore semaphoreGet;


    public Warehouse(int maxCountOfPizza) {
        semaphoreGet = new Semaphore(0, true);
        semaphorePut = new Semaphore(maxCountOfPizza, true);
    }

    public void putPizza(Pizza pizza) {
        try {
            semaphorePut.acquire();
            synchronized (queue) {
                queue.add(pizza);
            }
            semaphoreGet.release();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    public Pizza getPizza() {
        Pizza pizza = null;
        try {
            semaphoreGet.acquire();
            synchronized (queue) {
                pizza = queue.poll();
            }
            semaphorePut.release();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        return pizza;
    }
}
