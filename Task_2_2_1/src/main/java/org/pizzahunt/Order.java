package org.pizzahunt;

/**
 * Класс заказа.
 */
class Order {
    private final long id;

    /**
     * Конструктор.
     */
    Order(long id) {
        this.id = id;
    }

    /**
     * Геттер id.
     */
    long getId() {
        return id;
    }
}
