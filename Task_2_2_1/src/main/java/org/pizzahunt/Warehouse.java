package org.pizzahunt;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Класс, реализующий склад.
 */
class Warehouse {
    private final LinkedList<Pizza> queue = new LinkedList<>();
    private final Semaphore semaphorePut;
    private final Semaphore semaphoreGet;


    /**
     * Конструктор.
     */
    Warehouse(int maxCountOfPizza) {
        semaphoreGet = new Semaphore(0, true);
        semaphorePut = new Semaphore(maxCountOfPizza, true);
    }

    /**
     * Метод добавления пиццы на склад. Блокируется, если склад полон.
     */
    void putPizza(Pizza pizza) {
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

    /**
     * Метод взятия пиццы со склада. Блокируется, если на складе нет пицц.
     */
    Pizza getPizza() {
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
