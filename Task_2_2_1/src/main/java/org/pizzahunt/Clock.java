package org.pizzahunt;

/**
 * Класс, реализующий часы, отсчитывающий время работы пиццерии.
 */
public class Clock extends Thread {
    private boolean isClosed;
    private boolean isWorking;
    private Object lock;
    private int workTime;
    private PizzaHunt pizzaHunt;

    /**
     * Конструктор.
     */
    Clock(int time, PizzaHunt pizzaHunt) {
        isClosed = false;
        isWorking = false;
        workTime = time;
        this.pizzaHunt = pizzaHunt;
    }

    /**
     * Включение часов.
     */
    void turnOn() {
        if (isWorking) return;

        isWorking = true;
        if (lock == null) {
            lock = new Object();
            start();
            return;
        }

        synchronized (lock) {
            lock.notify();
        }
    }

    /**
     * Поднимаем флаг для завершения работы.
     */
    synchronized void brokeClock() {
        isClosed = true;
    }

    /**
     * Отсчитываем время рабочего дня.
     */
    @Override
    public void run() {
        isWorking = true;
        while (true) {
            if (isClosed) {
                Logger.write("clock is broken");
                return;
            }
            if (!isWorking) {
                synchronized (lock) {
                    try {
                        Logger.write("clock is waiting for start of new day");
                        lock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }

            try {
                sleep(workTime);
            } catch (InterruptedException ignored) {
            }

            Logger.write("WorkDay is ended");
            pizzaHunt.shutdown();

            isWorking = false;
        }
    }
}
