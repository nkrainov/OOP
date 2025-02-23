package org.example;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

class OrderQueue {
    private final LinkedList<Order> queue = new LinkedList<>();
    private final Semaphore semaphorePut;
    private final Semaphore semaphoreGet;

    public OrderQueue(int capacity) {
        semaphoreGet = new Semaphore(0, true);
        semaphorePut = new Semaphore(capacity, true);
    }

    public void makeOrder(long id) {
        try {
            semaphorePut.acquire();
            synchronized (queue) {
                queue.add(new Order(id));
            }
            semaphoreGet.release();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

    }

    public Order poll() {
        Order order = null;
        try {
            semaphoreGet.acquire();
            synchronized (queue) {
                order = queue.removeFirst();
            }
            semaphorePut.release();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        return order;
    }
}
