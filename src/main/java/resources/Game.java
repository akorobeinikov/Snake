package resources;


import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    int height = 20;
    int width = 20;
    int filled = 0;
    public Cell[][] game_field = new Cell[height][width]; // temporary public
    public Snake[] snakes = new Snake[2];
    public ArrayList<Integer> players = new ArrayList<>();
    public boolean status;

    public Game() {
        status = true;
        // field initialization
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                game_field[i][j] = new Cell(i, j, CellState.empty);
            }
        }

        // Snake initialization
        for (int i = 0; i < 2; ++i) {
            snakes[i] = null;
        }
    }

    public void addPlayer(int p_id) {
        players.add(p_id);
    }

    public int getSnakeIndex(int p_id) {
        return players.indexOf(p_id);
    }

    public ArrayList<Cell> addSnake() {
        int ok = 0;
        if (snakes[ok] != null) {
            ok++;
        }

        int snakePointy = new Random().nextInt(width);
        Boolean findy = false;
        while (!findy) {
            findy = true;
            for (int i = 0; i < height; i++) {
                if (game_field[i][snakePointy].state != CellState.empty) {
                    if (game_field[i][snakePointy].state == CellState.fromInteger(2 + ok)) {
                        snakePointy = (snakePointy+3)%width;
                    } else {
                        snakePointy = (snakePointy+1)%width;
                    }
                    break;
                }
            }
        }

        int snakePointx = 3 + new Random().nextInt(height-6);
        snakes[ok] = new Snake(snakePointx, snakePointy);

        ArrayList<Cell> snake = snakes[ok].translateSnakeToVectorOfCells();
        filled+=snake.size();
        for(int i = 0; i < snake.size(); i++) {
            snake.get(i).state = CellState.fromInteger(2 + ok);
            game_field[snake.get(i).x][snake.get(i).y].state = CellState.fromInteger(2 + ok);
        }
        return snake;
    }

    public Cell generateNewItem() {
        int point = new Random().nextInt(height * width - filled);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (game_field[i][j].isEmpty()) {
                    if (point == 0) {
                        game_field[i][j].state = CellState.eat;
                        return game_field[i][j];
                    } else {
                        point--;
                    }
                }
            }
        }
        return new Cell(-1, -1, null);
    }

    public ArrayList<Cell> getNotEmptyCells() {
        ArrayList<Cell> res = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(!game_field[i][j].isEmpty()) {
                    res.add(game_field[i][j]);
                }
            }
        }
        return res;
    }

    public SnakeChanges moveSnakeHead(int index) {
        filled++;
        Point head = snakes[index].moveHead();
        boolean is_grow = (game_field[head.x][head.y].state == CellState.eat);
        boolean is_crash = (game_field[head.x][head.y].state.ordinal() > 1);
        game_field[head.x][head.y].state = CellState.fromInteger(2 + index);
        Point tail = snakes[index].getTail();
        // what if next cell state is snake, but it's her own tail?
        boolean moving_to_tail = (game_field[tail.x][tail.y] == game_field[head.x][head.y]);
        SnakeChanges changes = new SnakeChanges(head, is_grow, moving_to_tail, is_crash);
        status = changes.status;
        return changes;
    }

    public Point moveSnakeTail(int index) {
        filled--;
        Point tail = snakes[index].moveTail();
        game_field[tail.x][tail.y].state = CellState.empty;
        return tail;
    }
}
