package org.pizzahunt;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Очередь заказов.
 */
class OrderQueue {
    private final LinkedList<Order> queue = new LinkedList<>();
    private final Semaphore semaphorePut;
    private final Semaphore semaphoreGet;

    /**
     * Конструктор.
     */
    OrderQueue(int capacity) {
        semaphoreGet = new Semaphore(0, true);
        semaphorePut = new Semaphore(capacity, true);
    }

    /**
     * Метод добавления заказа в очередь. Блокируется, если в очереди нет места.
     */
    void makeOrder(Order order) {
        try {
            semaphorePut.acquire();
            synchronized (queue) {
                queue.add(order);
            }
            semaphoreGet.release();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

    }

    /**
     * Метод взятия заказа из очереди. Блокируется, если в очереди нет заказов.
     */
    Order poll() {
        Order order = null;
        try {
            semaphoreGet.acquire();
            synchronized (queue) {
                order = queue.remove(0);
            }
            semaphorePut.release();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        return order;
    }
}
