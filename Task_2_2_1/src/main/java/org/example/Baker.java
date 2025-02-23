package org.example;

import java.util.Random;

class Baker extends Thread{
    private final Warehouse warehouse;
    private final OrderQueue orderQueue;
    private final int speed;

    public Baker(Warehouse warehouse, OrderQueue orderQueue, int speed) {
        this.warehouse = warehouse;
        this.orderQueue = orderQueue;
        this.speed = speed;
    }

    @Override
    public void run() {
        Random random = new Random();
        boolean flag = false;
        while (true) {
            if (interrupted()) {
                synchronized (this) {
                    try {
                        System.out.println("Baker want to sleep");
                        wait();
                    } catch (InterruptedException ignored) {}
                }
            }
            flag = false;

            Order order = orderQueue.poll();
            if (order == null) {
                continue;
            }
            int bakingTime = random.nextInt(speed);
            flag = interrupted();
            try {
                sleep(bakingTime);
            } catch (InterruptedException ignored) {}
            if (flag) interrupt();
            Pizza pizza = new Pizza(order);
            warehouse.putPizza(pizza);
        }
    }
}
