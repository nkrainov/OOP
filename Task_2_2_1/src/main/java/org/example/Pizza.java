package org.example;

class Pizza {
    private final long id;

    public Pizza(Order order) {
        this.id = order.getId();
    }

    public long getId() {
        return id;
    }
}
