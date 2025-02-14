package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Параллельное решение задачи.
 */
public class ThreadChecker {
    private static int countOfWorkers = 1;
    private static final Object lock = new Object();

    /**
     * Сеттер для количества рабочих потоков.
     */
    public static void setCountOfWorkers(int newCountOfWorkers) {
        if (countOfWorkers >= 1) {
            countOfWorkers = newCountOfWorkers;
        }
    }

    /**
     * Реализация потока, проверяющие вхождение составного числа в части массива.
     */
    private static class Worker extends Thread {
        private int[] numbers;
        private boolean result = false;
        private boolean done = false;

        public boolean isDone() {
            return done;
        }

        public boolean getResult() {
            return result;
        }

        Worker(int[] numbers) {
            this.numbers = numbers;
        }

        @Override
        public void run() {
            for (int number : numbers) {
                int sqrt = (int) Math.ceil(Math.sqrt(number));
                for (int i = 2; i <= sqrt; i++) {
                    if (number % i == 0) {
                        result = true;
                        done = true;
                        synchronized (lock) {
                            lock.notify();
                        }
                        return;
                    }
                }

                if (interrupted()) {
                    return;
                }
            }

            result = false;
            done = true;
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    /**
     * Метод, реализующий решение задачи.
     */
    public static boolean checkCompositeNumbers(int[] numbers) {
        ArrayList<Worker> workers = new ArrayList<>();
        int step = numbers.length / countOfWorkers;
        if (step == 0) {
            step = 1;
        }

        for (int i = 0; i < countOfWorkers; i++) {
            if (i * step >= numbers.length) {
                break;
            }

            int start = i * step;
            int end = (i + 1) * step < numbers.length ? start + step : numbers.length;

            int[] workerNumbers = Arrays.copyOfRange(numbers, start, end);
            Worker worker = new Worker(workerNumbers);
            workers.add(worker);
            worker.start();
        }


        while (!workers.isEmpty()) {
            synchronized (lock) {
                try {
                    lock.wait(10000);
                } catch (InterruptedException ignored) {
                }
            }

            for (int i = workers.size() - 1; i >= 0; i--) {
                if (workers.get(i).isDone()) {
                    if (workers.get(i).getResult()) {
                        killWorkers(workers);
                        return true;
                    } else {
                        workers.remove(i);
                    }
                }
            }
        }

        return false;

    }

    private static void killWorkers(Collection<Worker> workers) {
        for (Worker worker : workers) {
            worker.interrupt();
        }
    }
}
