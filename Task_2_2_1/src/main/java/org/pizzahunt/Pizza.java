package org.pizzahunt;

/**
 * Класс пиццы.
 */
class Pizza {
    private final long id;

    /**
     * Конструктор.
     */
    Pizza(Order order) {
        this.id = order.getId();
    }

    /**
     * Геттер id.
     */
    long getId() {
        return id;
    }
}
