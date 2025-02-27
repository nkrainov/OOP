package org.pizzahunt;

import java.util.Random;

/**
 * Класс, реализующий пекаря.
 */
class Baker extends Thread {
    private final BlockedQueue<Pizza> warehouse;
    private final BlockedQueue<Order> orderQueue;
    private final int maxTimeForCooking;
    private volatile boolean isDismissal;
    private final String name;

    /**
     * Конструктор.
     *
     * @param name              name пекаря.
     * @param maxTimeForCooking максимальное время, которое тратится на приготовление пиццы.
     * @param orderQueue        очередь, из которой берутся заказы.
     * @param warehouse         склад, куда отправляются пиццы.
     */
    Baker(String name, BlockedQueue<Pizza> warehouse,
          BlockedQueue<Order> orderQueue, int maxTimeForCooking) {
        this.name = name;
        this.warehouse = warehouse;
        this.orderQueue = orderQueue;
        this.maxTimeForCooking = maxTimeForCooking;
        isDismissal = false;
    }

    /**
     * Алгоритм работы пекаря таков: берем заказ из очереди, ждем случайное время
     * (но не дольше maxTimeForCooking), имитируя таким образом приготовление пиццы,
     * затем кладем пиццу на склад.
     * Если рабочий день закончился, то мы доделываем текущий заказ, после ожидаем пробуждения.
     * Если пиццерия завершит работу, то и поток завершит своё выполнение.
     */
    @Override
    public void run() {
        Random random = new Random();
        boolean flag = false;
        while (true) {
            if (interrupted()) {
                synchronized (this) {
                    try {
                        Logger.write("baker " + name + " has completed his workday");
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            if (isDismissal) {
                Logger.write("baker " + name + " was dismissal");
                return;
            }

            Logger.write("baker " + name + " try to get order");
            Order order = orderQueue.poll();
            if (order == null) {
                continue;
            }
            Logger.write("baker " + name + " just received order " + order.getId() + " and started cooking");
            int bakingTime = random.nextInt(maxTimeForCooking);
            flag = interrupted();
            try {
                sleep(bakingTime);
            } catch (InterruptedException ignored) {
            }
            if (flag) interrupt();

            Pizza pizza = new Pizza(order);

            Logger.write("baker " + name + " try to put pizza " + pizza.getId() + " to warehouse");
            warehouse.add(pizza);
            Logger.write("baker " + name + " putted pizza " + pizza.getId() + " to warehouse");
        }
    }

    /**
     * Возвращает true, если работник уволен (пиццерия закрыта).
     */
    boolean isDismissal() {
        return isDismissal;
    }

    /**
     * Увольняет пекаря. Приводит к завершению работы после выполнения текущего заказа.
     */
    synchronized void dismiss() {
        isDismissal = true;
        notify();
    }
}
