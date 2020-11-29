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

    public Game() {
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
                    if (game_field[i][snakePointy].state == CellState.snake) {
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
            game_field[snake.get(i).x][snake.get(i).y].state = CellState.snake;
        }
        return snake;
    }

    public void setCell(Cell new_c) {
        game_field[new_c.x][new_c.y] = new_c;
    }

    public Cell generateNewItem() {
        int point = new Random().nextInt(height * width - filled);
        System.out.println(point);
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

    public void increaseSnake(int index) {
        filled++;
        snakes[index].increaseSnake();
    }
}
