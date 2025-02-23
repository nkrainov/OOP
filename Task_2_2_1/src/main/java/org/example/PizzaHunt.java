package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PizzaHunt extends Thread {
    private boolean isWorking;
    private Long id = 0L;
    private Object lock = null;
    private OrderQueue queue;
    private ArrayList<Baker> bakers;
    private ArrayList<Courier> couriers;
    private Warehouse warehouse;
    private Configuration conf;
    private int workDay;

    public PizzaHunt(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        conf = objectMapper.readValue(new File(path), Configuration.class);
        queue = new OrderQueue(conf.capacityOfOrderQueue);
        warehouse = new Warehouse(conf.capacityOfWarehouse);
        workDay = conf.timeOfWorking;
        initBakers();
        initCouriers();
    }

    private void initBakers() {
        bakers = new ArrayList<>();
        for (int i = 0; i < conf.countOfBakers; i++) {
            bakers.add(new Baker(warehouse, queue, conf.maxBakingTime));
        }
    }

    private void initCouriers() {
        couriers = new ArrayList<>();
        for (int i = 0; i < conf.countOfCouriers; i++) {
            couriers.add(new Courier(warehouse, conf.trunksCapacity[i], conf.maxCourierTime));
        }
    }

    public void startWorkDay() {
        isWorking = true;
        if (lock == null) {
            lock = new Object();

            for (Baker baker : bakers) {
                baker.start();
            }

            for (Courier courier : couriers) {
                courier.start();
            }

            start();
            return;
        }

        for (Baker baker : bakers) {
            synchronized (baker) {
                baker.notify();
            }
        }

        for (Courier courier : couriers) {
            synchronized (courier) {
                courier.notify();
            }
        }

        synchronized (lock) {
            lock.notify();
        }
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void makeOrder() {
        if (isWorking) {
            synchronized (id) {
                queue.makeOrder(id);
                id++;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!isWorking) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {}
                }
            }

            try {
                sleep(workDay);
            } catch (InterruptedException ignored) {}

            shutdown();

            isWorking = false;
        }
    }

    private void shutdown() {
        for (Baker baker : bakers) {
            baker.interrupt();
        }

        for (Courier courier : couriers) {
            courier.interrupt();
        }
    }
}
