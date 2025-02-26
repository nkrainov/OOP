package org.pizzahunt;

/**
 * Класс конфигурации, описывающий формат json-файла для инициализации пиццерии.
 */
public class Configuration {
    public int countOfBakers = 1;
    public int countOfCouriers = 1;
    public int timeOfWorking = 1000000;
    public int capacityOfOrderQueue = 5;
    public int capacityOfWarehouse = 5;
    public int maxBakingTime = 100;
    public int maxCourierTime = 10000;
    public int[] trunksCapacity = {1};
}
