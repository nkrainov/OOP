package org.pizzahunt;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Собственная частичная реализация BlockedQueue.
 */
class BlockedQueue<E> {
    private final LinkedList<E> queue = new LinkedList<>();
    private final Semaphore semaphorePut;
    private final Semaphore semaphoreGet;


    /**
     * Конструктор.
     */
    BlockedQueue(int capacity) {
        semaphoreGet = new Semaphore(0, true);
        semaphorePut = new Semaphore(capacity, true);
    }

    /**
     * Метод добавления объекта в очередь. Блокируется, если очередь полна.
     * Не очищает статус прерывания потока в случае, если оно произошло.
     */
    void add(E object) {
        try {
            semaphorePut.acquire();
            synchronized (queue) {
                queue.add(object);
            }
            semaphoreGet.release();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Метод взятия объекта из очереди. Блокируется, если в очереди нет объектов.
     * Не очищает статус прерывания потока в случае, если оно произошло.
     */
    E poll() {
        E object = null;
        try {
            semaphoreGet.acquire();
            synchronized (queue) {
                object = queue.poll();
            }
            semaphorePut.release();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        return object;
    }
}
