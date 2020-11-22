package resources;


import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    int height = 20;
    int width = 20;
    int filled = 0;
    Cell[][] game_field = new Cell[height][width]; // temporary public
    Snake[] snakes = new Snake[2];

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
        while (true) {
            int snakePointx = 1 + new Random().nextInt(19);
            int snakePointy = 1 + new Random().nextInt(19);
            if (game_field[snakePointx][snakePointy].isEmpty()) {
                snakes[ok] = new Snake(snakePointx, snakePointy);
                break;
            }
        }
        filled++;
        return snakes[ok].translateSnakeToVectorOfCells();
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
}
