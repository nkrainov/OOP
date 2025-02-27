package org.pizzahunt.configuration;

import java.util.List;

/**
 * Класс конфигурации, описывающий формат json-файла для инициализации пиццерии.
 */
public class Configuration {
    public int timeOfWorking = 1000000;
    public int capacityOfOrderQueue = 5;
    public int capacityOfWarehouse = 5;
    public List<BakerConf> bakers;
    public List<CourierConf> couriers;
}
