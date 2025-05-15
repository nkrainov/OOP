package org.pizzahunt;

/**
 * Абстрактный класс, реализующий общее представление о работнике.
 */
abstract class Worker extends Thread {
    private String name = null;
    private volatile Boolean isDismissal = false;

    /**
     * Увольнение работника.
     */
    synchronized void dismiss() {
        isDismissal = true;
        notify();
    }

    /**
     * Сеттер имени работника.
     */
    void setWorkerName(String name) {
        this.name = name;
    }

    /**
     * Геттер имени.
     */
    String getWorkerName() {
        return name;
    }

    /**
     * Если мы не уволены, то засыпаем, ожидая следующего дня.
     */
    void sleepUntilNewDay() {
        synchronized (this) {
            if (isDismissal) {
                return;
            }

            if (interrupted()) {
                try {
                    Logger.write("worker " + name + " has completed his workday");
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    /**
     * Действия при увольнении.
     */
    void weAreDismissal() {
        Logger.write("worker " + name + " was dismissal");
    }

    /**
     * Получение задачи.
     */
    abstract Object getTask();

    /**
     * Выполнение задачи.
     */
    abstract Object doTask(Object task);

    /**
     * Сообщение о результатах работы.
     */
    abstract void showResult(Object result);

    /**
     * Алгоритм работы работника прост: он получает на каждой итерации задачу, её выполняет,
     * затем сообщает свои результаты работы.
     */
    @Override
    public void run() {
        while (true) {

            sleepUntilNewDay();

            synchronized (this) {
                if (isDismissal) {
                    weAreDismissal();
                    return;
                }
            }

            Object task = getTask();

            Object result = doTask(task);

            showResult(result);
        }
    }
}
