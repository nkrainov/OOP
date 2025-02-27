package org.pizzahunt;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import org.pizzahunt.configuration.BakerConf;
import org.pizzahunt.configuration.Configuration;
import org.pizzahunt.configuration.CourierConf;
import org.pizzahunt.exceptions.InvalidFormatException;

/**
 * Класс, реализующий работу пиццерии. Создает все необходимые объекты для работы.
 */
public class PizzaHunt {
    private volatile boolean isWorking;
    private volatile Long id = 0L;
    private final BlockedQueue<Order> queue;
    private ArrayList<Baker> bakers;
    private ArrayList<Courier> couriers;
    private final BlockedQueue<Pizza> warehouse;
    private final Configuration conf;
    private boolean isStarted = false;
    private volatile boolean isClosed;
    private final Clock clock;

    /**
     * Инициализирует объекты, необходимые для работы пиццерии.
     * Их настройки берутся из файла в формате JSON.
     *
     * @param json - json-файл со структурой, определенной в классе Configuration.
     * @param log  - OutputStream, куда записываются логи объектов в работе пиццерии.
     */
    public PizzaHunt(File json, OutputStream log) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        conf = objectMapper.readValue(json, Configuration.class);

        if (conf.bakers == null || conf.bakers.isEmpty()) {
            throw new InvalidFormatException("Count of bakers is zero");
        }
        if (conf.couriers == null || conf.couriers.isEmpty()) {
            throw new InvalidFormatException("Count of couriers is zero");
        }

        if (conf.capacityOfOrderQueue <= 0) {
            throw new InvalidFormatException("Capacity of order queue is zero");
        }

        if (conf.capacityOfWarehouse <= 0) {
            throw new InvalidFormatException("Capacity of warehouse is zero");
        }

        queue = new BlockedQueue<>(conf.capacityOfOrderQueue);
        warehouse = new BlockedQueue<>(conf.capacityOfWarehouse);
        clock = new Clock(conf.timeOfWorking, this);

        initBakers();
        initCouriers();

        Logger.setOutputStream(log);
        Logger.write("init PizzaHunt");
        isClosed = false;
    }

    /**
     * Начинает рабочий день.
     * Если рабочий день в процессе или пиццерия закрыта (isClosed), то ничего не делает.
     * В ином случае запускает или пробуждает потоки основных объектов (Clock, Baker, Courier).
     */
    public synchronized void startWorkDay() {
        if (isWorking || isClosed) {
            return;
        }

        isWorking = true;

        if (!isStarted) {
            isStarted = true;

            for (Baker baker : bakers) {
                baker.start();
            }

            for (Courier courier : couriers) {
                courier.start();
            }

            clock.turnOn();
            return;
        }

        for (Baker baker : bakers) {
            baker.notify();
        }

        for (Courier courier : couriers) {
            courier.notify();
        }

        clock.turnOn();
        Logger.write("Workday started");
    }

    /**
     * Закрывает пиццерию. Потоки основных объектов завершают свое выполнение.
     * Если пиццерия уже закрыта, то ничего не делает.
     */
    public synchronized void closePizzaHunt() throws InterruptedException {
        if (isClosed) {
            return;
        }

        if (isWorking) {
            wait();
        }

        clock.brokeClock();

        for (Baker baker : bakers) {
            baker.dismiss();
            baker.join();
        }

        for (Courier courier : couriers) {
            courier.dismiss();
            courier.join();
        }

        Logger.write("PizzaHunt is closed");
        isClosed = true;
    }

    /**
     * Возвращает true, если рабочий день в процессе.
     */
    public synchronized boolean isWorking() {
        return isWorking;
    }

    /**
     * Добавляет заказ в очередь заказов.
     */
    public synchronized void makeOrder() {
        if (isWorking) {
            queue.add(new Order(id));
            id++;
        }
    }

    /**
     * Делает interrupt ко всем потокам.
     */
    synchronized void shutdown() {
        for (Baker baker : bakers) {
            baker.interrupt();
        }

        for (Courier courier : couriers) {
            courier.interrupt();
        }

        this.notify();
    }

    /**
     * Создает объекты Baker.
     */
    private void initBakers() {
        bakers = new ArrayList<>();
        for (BakerConf bakerConf : conf.bakers) {
            if (bakerConf.name == null || bakerConf.name.isEmpty()
                    || bakerConf.maxTimeOfCooking <= 0) {
                throw new InvalidFormatException("invalid settings for courier");
            }
            bakers.add(new Baker(bakerConf.name, warehouse, queue, bakerConf.maxTimeOfCooking));
        }
    }

    /**
     * Создает объекты Courier.
     */
    private void initCouriers() {
        couriers = new ArrayList<>();
        for (CourierConf courierConf : conf.couriers) {
            if (courierConf.name == null || courierConf.name.isEmpty()
                || courierConf.maxTimeOfDelivery <= 0 || courierConf.trunkCapacity <= 0) {
                throw new InvalidFormatException("invalid settings for courier");
            }

            couriers.add(new Courier(courierConf.name,
                                    warehouse,
                                    courierConf.trunkCapacity,
                                    courierConf.maxTimeOfDelivery));
        }
    }
}
