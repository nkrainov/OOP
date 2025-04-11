package org.example.snake;

public interface Game {

    public void init();

    public boolean tick();

    public Object update(Direction dir);
}
