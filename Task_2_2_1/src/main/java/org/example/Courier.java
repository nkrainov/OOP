package org.example;

import java.util.Random;

class Courier extends Thread {
    private Warehouse warehouse;
    private int trunkCapacity;
    private int maxTime;

    public Courier(Warehouse warehouse, int trunkCapacity, int time) {
        this.warehouse = warehouse;
        this.trunkCapacity = trunkCapacity;
        this.maxTime = time;
    }

    @Override
    public void run() {
        Random rand = new Random();
        Pizza[] pizzas = new Pizza[trunkCapacity];
        while (true) {
            if (interrupted()) {
                synchronized (this) {
                    try {
                        System.out.println("Courier want to sleep");
                        wait();
                    } catch (InterruptedException ignored) {}
                }
            }

            int countNotNull = 0;
            for (int i = 0; i < trunkCapacity; i++) {
                pizzas[i] = warehouse.getPizza();
                if (pizzas[i] == null) {
                    break;
                }
                countNotNull++;
            }

            boolean flag = interrupted();
            for (int i = 0; i < countNotNull; i++) {
                int time = rand.nextInt(maxTime);

                try {
                    sleep(time);
                } catch (InterruptedException ignored) {}

                System.out.println("Pizza " + pizzas[i].getId() + "on the target");
            }
            if (flag) {
                interrupt();
            }

        }
    }
}
