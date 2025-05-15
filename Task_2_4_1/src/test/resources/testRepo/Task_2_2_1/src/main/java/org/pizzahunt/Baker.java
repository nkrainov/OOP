package org.pizzahunt;

import java.util.Random;

/**
 * Класс, реализующий пекаря.
 */
class Baker extends Worker {
    private final BlockedQueue<Pizza> warehouse;
    private final BlockedQueue<Order> orderQueue;
    private final int maxTimeForCooking;
    private Random random = new Random();

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
        setWorkerName(name);
        this.warehouse = warehouse;
        this.orderQueue = orderQueue;
        this.maxTimeForCooking = maxTimeForCooking;
    }

    /**
     * Реализация получения задачи.
     */
    @Override
    Object getTask() {
        Logger.write("baker " + getWorkerName() + " try to get order");
        Order order = orderQueue.poll();
        if (order == null) {
            Logger.write("baker " + getWorkerName() + " not received order");
            return null;
        }

        Logger.write("baker " + getWorkerName() + " just received order "
                + order.getId() + " and started cooking");

        return order;
    }

    /**
     * Реализация выполнения задачи.
     */
    @Override
    Object doTask(Object task) {
        if (!(task instanceof Order)) {
            return null;
        }

        int timeForCooking = random.nextInt(maxTimeForCooking);
        try {
            sleep(timeForCooking);
        } catch (InterruptedException e) {
            interrupt();
        }

        return new Pizza((Order) task);
    }

    /**
     * Реализация сообщения результата.
     */
    @Override
    void showResult(Object result) {
        if (!(result instanceof Pizza)) {
            return;
        }
        Logger.write("baker " + getWorkerName()
                + " try to put pizza " + ((Pizza) result).getId() + " to warehouse");

        warehouse.add((Pizza) result);

        Logger.write("baker " + getWorkerName()
                + " putted pizza " + ((Pizza) result).getId() + " to warehouse");
    }
}
