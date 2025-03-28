package org.example.snake;

public class Field {
    public enum Direction {
        Up, Down, Left, Right
    }

    static public class Cell {
        public enum CellState {
            Snake,
            Food,
            Empty
        }

        private CellState state;
        private int counter;

        public Cell() {
            state = CellState.Empty;
            counter = 0;
        }

        public void setSnake(int counter) {
            state = CellState.Snake;
            this.counter += counter;
        }

        public void setFood() {
            state = CellState.Food;
        }

        public void decrement() {
            if (state != CellState.Snake) {
                return;
            }

            counter--;
            if (counter == 0) {
                state = CellState.Empty;
            }

        }

        public CellState getState() {
            return state;
        }
    }

    private Cell[][] cells;
    private GameMain game;

    private int head_x;
    private int head_y;
    private int curSnakeLen;

    public Field(GameMain game) {
        this.game = game;
        cells = new Cell[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                cells[y][x] = new Cell();
            }
        }
    }

    public void setSnake(int x, int y) {
        if (x >= 10 || y >= 10 || x < 0 || y < 0) {
            throw new IllegalArgumentException("x or y out of borders");
        }

        cells[y][x].setSnake(1);
        head_x = x;
        head_y = y;
        curSnakeLen = 1;
    }

    public void setFood(int x, int y) {
        cells[y][x].setFood();
    }

    public boolean move(Direction direction) {
        switch (direction) {
            case Up:
                head_y = head_y - 1;
                head_y = head_y < 0 ? 9 : head_y;
                break;
            case Down:
                head_y = (head_y + 1) % 10;
                break;
            case Left:
                head_x = head_x - 1;
                head_x = head_x < 0 ? 9 : head_x;
                break;
            case Right:
                head_x = (head_x + 1) % 10;
                break;
        }


        if (cells[head_x][head_y].state == Cell.CellState.Food) {
            curSnakeLen++;
            cells[head_x][head_y].setSnake(curSnakeLen);
            game.decrementFood();
            return true;
        }

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.decrement();
            }
        }
        cells[head_x][head_y].setSnake(curSnakeLen);

        return cells[head_x][head_y].counter <= curSnakeLen;
    }

    public Cell[][] getCells() {
        return cells;
    }


}
