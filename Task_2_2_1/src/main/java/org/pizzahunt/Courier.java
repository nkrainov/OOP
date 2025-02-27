package org.pizzahunt;

import java.util.Random;

/**
 * Класс, реализующий курьера.
 */
class Courier extends Thread {
    private BlockedQueue<Pizza> warehouse;
    private int trunkCapacity;
    private int maxmaxTimeForDelivering;
    private volatile boolean isDismissal;
    private final String name;

    /**
     * Конструктор.
     *
     * @param name                 имя пекаря.
     * @param trunkCapacity        вместимость багажника.
     * @param maxTimeForDelivering максимальное время, которое может доставляться отдельная пицца.
     * @param warehouse            склад, откуда берутся пиццы.
     */
    Courier(String name, BlockedQueue<Pizza> warehouse, int trunkCapacity, int maxTimeForDelivering) {
        this.warehouse = warehouse;
        this.trunkCapacity = trunkCapacity;
        this.maxmaxTimeForDelivering = maxTimeForDelivering;
        isDismissal = false;
        this.name = name;
    }

    /**
     * Алгоритм работы курьера таков: берем определенное количество пицц (не больше trunkCapacity)
     * из очереди, ждем случайное время (но не дольше maxTimeForDelivering),
     * имитируя таким образом доставку пиццы, записываем в логи факт окончания доставки.
     * Если рабочий день закончился, то мы доделываем текущий заказ, после ожидаем пробуждения.
     * Если пиццерия завершит работу, то и поток завершит своё выполнение.
     */
    @Override
    public void run() {
        Random rand = new Random();
        Pizza[] pizzas = new Pizza[trunkCapacity];
        while (true) {
            if (interrupted()) {
                synchronized (this) {
                    try {
                        Logger.write("courier " + name + " has completed his workday");
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            if (isDismissal) {
                Logger.write("courier " + name + " was dismissal");
                return;
            }

            Logger.write("courier " + name + " try to get pizzas");
            int countNotNull = 0;
            for (int i = 0; i < trunkCapacity; i++) {
                pizzas[i] = warehouse.poll();
                if (pizzas[i] == null) {
                    break;
                }
                countNotNull++;
            }
            Logger.write("courier " + name + " got " + countNotNull + " pizza(s)");

            boolean flag = interrupted();

            for (int i = 0; i < countNotNull; i++) {
                Logger.write("courier " + name + " start delivering pizzas " + pizzas[i].getId());
                int maxTimeForDelivering = rand.nextInt(maxmaxTimeForDelivering);

                try {
                    sleep(maxTimeForDelivering);
                } catch (InterruptedException ignored) {
                    interrupt();
                }

                Logger.write("courier " + name + " delivered pizza " + pizzas[i].getId());

            }

            if (flag) {
                interrupt();
            }

        }
    }

    /**
     * Возвращает true, если работник уволен (пиццерия закрыта).
     */
    boolean isDismissal() {
        return isDismissal;
    }

    /**
     * Увольняет курьера. Приводит к завершению работы после выполнения текущего заказа.
     */
    synchronized void dismiss() {
        isDismissal = true;
        notify();
    }
}
