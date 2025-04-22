package org.example.snake;

public interface Game {

    public Object init();

    public boolean tick();

    public Object update(Direction dir);

    public boolean victory();
}
