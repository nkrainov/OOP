package org.pizzahunt;

import java.util.Random;

/**
 * Класс, реализующий пекаря.
 */
class Baker extends Thread{
    private final Warehouse warehouse;
    private final OrderQueue orderQueue;
    private final int maxTimeForCooking;
    private volatile boolean isDismissal;
    private final int id;

    /**
     * Конструктор.
     * @param id id пекаря.
     * @param maxTimeForCooking максимальное время, которое тратится на приготовление пиццы.
     * @param orderQueue очередь, из которой берутся заказы.
     * @param warehouse склад, куда отправляются пиццы.
     */
    Baker(int id, Warehouse warehouse, OrderQueue orderQueue, int maxTimeForCooking) {
        this.id = id;
        this.warehouse = warehouse;
        this.orderQueue = orderQueue;
        this.maxTimeForCooking = maxTimeForCooking;
        isDismissal = false;
    }

    /**
     * Алгоритм работы пекаря таков: берем заказ из очереди, ждем случайное время
     * (но не дольше maxTimeForCooking), имитируя таким образом приготовление пиццы, затем кладем пиццу на склад.
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
                        Logger.write("baker " + id + " has completed his workday");
                        wait();
                    } catch (InterruptedException ignored) {}
                }
            }

            if (isDismissal) {
                Logger.write("baker " + id + " was dismissal");
                return;
            }

            Logger.write("baker " + id + " try to get order");
            Order order = orderQueue.poll();
            if (order == null) {
                continue;
            }
            Logger.write("baker " + id + " just received order " + order.getId() + " and started cooking");
            int bakingTime = random.nextInt(maxTimeForCooking);
            flag = interrupted();
            try {
                sleep(bakingTime);
            } catch (InterruptedException ignored) {}
            if (flag) interrupt();

            Pizza pizza = new Pizza(order);

            Logger.write("baker " + id + " try to put pizza " + pizza.getId() + " to warehouse");
            warehouse.putPizza(pizza);
            Logger.write("baker " + id + " putted pizza " + pizza.getId() + " to warehouse");
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
