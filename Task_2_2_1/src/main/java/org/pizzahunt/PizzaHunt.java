package org.pizzahunt;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Класс, реализующий работу пиццерии. Создает все необходимые объекты для работы.
 */
public class PizzaHunt {
    private volatile boolean isWorking;
    private volatile Long id = 0L;
    private final OrderQueue queue;
    private ArrayList<Baker> bakers;
    private ArrayList<Courier> couriers;
    private final Warehouse warehouse;
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
        queue = new OrderQueue(conf.capacityOfOrderQueue);
        warehouse = new Warehouse(conf.capacityOfWarehouse);
        isClosed = false;
        clock = new Clock(conf.timeOfWorking, this);
        initBakers();
        initCouriers();
        Logger.setOutputStream(log);
        Logger.write("init PizzaHunt");
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

        clock.brokeClock();

        for (Baker baker : bakers) {
            baker.dismiss();
            baker.join();
        }

        for (Courier courier : couriers) {
            courier.dismiss();
            courier.join();
        }

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
            queue.makeOrder(new Order(id));
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
    }

    /**
     * Создает объекты Baker.
     */
    private void initBakers() {
        bakers = new ArrayList<>();
        for (int i = 0; i < conf.countOfBakers; i++) {
            bakers.add(new Baker(i, warehouse, queue, conf.maxBakingTime));
        }
    }

    /**
     * Создает объекты Courier.
     */
    private void initCouriers() {
        couriers = new ArrayList<>();
        for (int i = 0; i < conf.countOfCouriers; i++) {
            couriers.add(new Courier(i, warehouse, conf.trunksCapacity[i], conf.maxCourierTime));
        }
    }
}
