package org.pizzahunt;

import java.util.ArrayList;
import java.util.Random;

/**
 * Класс, реализующий курьера.
 */
class Courier extends Worker {
    private BlockedQueue<Pizza> warehouse;
    private int trunkCapacity;
    private int maxmaxTimeForDelivering;
    private Random random = new Random();

    /**
     * Конструктор.
     *
     * @param name                 имя пекаря.
     * @param trunkCapacity        вместимость багажника.
     * @param maxTimeForDelivering максимальное время, которое может доставляться отдельная пицца.
     * @param warehouse            склад, откуда берутся пиццы.
     */
    Courier(String name, BlockedQueue<Pizza> warehouse,
            int trunkCapacity, int maxTimeForDelivering) {
        this.warehouse = warehouse;
        this.trunkCapacity = trunkCapacity;
        this.maxmaxTimeForDelivering = maxTimeForDelivering;
        setWorkerName(name);
    }

    /**
     * Реализация получения задачи.
     */
    @Override
    Object getTask() {
        ArrayList<Pizza> pizzas = new ArrayList<Pizza>();
        Logger.write("courier " + getWorkerName() + " try to get pizzas");

        for (int i = 0; i < trunkCapacity; i++) {
            Pizza pizza = warehouse.poll();
            if (pizza == null) {
                return null;
            }
            pizzas.add(pizza);
        }
        Logger.write("courier " + getWorkerName() + " got "
                + pizzas.size() + " pizza(s)");

        return pizzas;
    }

    /**
     * Реализация выполнения задачи.
     */
    @Override
    Object doTask(Object task) {
        if (!(task instanceof ArrayList)) {
            return null;
        }

        @SuppressWarnings("unchecked")
        ArrayList<Pizza> pizzas = (ArrayList<Pizza>) task;
        for (int i = 0; i < pizzas.size(); i++) {
            Logger.write("courier " + getWorkerName()
                    + " start delivering pizzas " + pizzas.get(i).getId());
            int maxTimeForDelivering = random.nextInt(maxmaxTimeForDelivering);

            try {
                sleep(maxTimeForDelivering);
            } catch (InterruptedException ignored) {
                interrupt();
            }

            Logger.write("courier " + getWorkerName()
                    + " delivered pizza " + pizzas.get(i).getId());
        }

        return new Object();
    }

    /**
     * Класс, реализующий сообщение результата.
     */
    @Override
    void showResult(Object result) {
        if (result == null) {
            return;
        }

        Logger.write("courier " + getWorkerName() + " delivered pizzas");
    }
}
