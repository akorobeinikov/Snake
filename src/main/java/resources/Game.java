package resources;


import java.util.Random;

public class Game {
    int height = 20;
    int width = 20;
    int filled = 0;
    Cell[][] game_field = new Cell[height][width];
    Snake[] snakes = new Snake[2];

    public Game() {
        // field initialization
        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                game_field[i][j] = new Cell(i, j, "empty");
            }
        }

        // Snake initialization
        for(int i = 0; i < 2; ++i) {
            snakes[i] = null;
        }
    }

    public void addSnake() {
        int ok = 0;
        if(snakes[ok] != null) {
            ok++;
        }
        while(true) {
            int snakePointx = 1 + new Random().nextInt(19);
            int snakePointy = 1 + new Random().nextInt(19);
            if (game_field[snakePointx][snakePointy].isEmpty()) {
                snakes[ok] = new Snake(snakePointx, snakePointy);
                break;
            }
        }
        filled++;
    }
}
