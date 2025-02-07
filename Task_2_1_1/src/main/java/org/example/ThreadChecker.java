package org.example;

import java.util.ArrayList;
import java.util.Arrays;

/***
 Параллельное решение задачи.
 */
public class ThreadChecker {
    private static int countOfWorkers = 1;

    /***
     Сеттер для количества рабочих потоков.
     */
    public static void setCountOfWorkers(int newCountOfWorkers) {
        if (countOfWorkers >= 1) {
            countOfWorkers = newCountOfWorkers;
        }
    }

    /***
     Реализация потока, проверяющие вхождение составного числа в части массива.
     */
    private static class Worker extends Thread {
        private int[] numbers;
        private boolean result;

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
                        return;
                    }
                }

                if (interrupted()) {
                    return;
                }
            }

            result = false;
        }
    }

    /***
     Метод, реализующий решение задачи.
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


        try {
            for (Worker worker : workers) {
                worker.join();
                if (worker.getResult()) {
                    for (Worker worker2 : workers) {
                        worker2.interrupt();
                    }
                    return true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
}
