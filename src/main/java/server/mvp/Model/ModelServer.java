package server.mvp.Model;

import java.util.ArrayList;
import java.util.Random;


import resources.Cell;
import resources.CellState;
import resources.Game;
import resources.SnakeChanges;
import server.mvp.Presenter.IPresenter;


class ModelServer implements IModelServer{

    ArrayList<Game> games = new ArrayList<>();
    ArrayList<IPresenter> list_players = new ArrayList<>();
    Cell buffer;

    public ModelServer() {
        games.add(new Game());
        buffer = new Cell(0, 0, CellState.empty);
    }

    public void setCell(Cell new_c) {
        games.get(0).setCell(new_c);
        buffer = new_c;
        refresh();
    }

    public void gameInitialization(int gameId) {
        buffer = games.get(gameId).generateNewItem(); // what if return is (-1, -1, -1)?
        refresh();
        ArrayList<Cell> snake = games.get(gameId).addSnake();
        for (int i = 0; i < snake.size(); ++i) {
            buffer = snake.get(i);
            refresh();
        }
    }

    public void gameStart(int gameId) {
        new Thread(){
            @Override
            public void run() {
                Game game = games.get(gameId);
                boolean status = false;
                while(true) {
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < game.snakes.length; ++i) {
                        if (game.snakes[i] == null) {
                            continue;
                        }
                        SnakeChanges changes = game.snakes[i].move();
                        status = changes.status;
                        System.out.printf("Status = %b \n", status);
                        if (status) {
                            System.out.println("GAME OVER");
                            break;
                        }
                        buffer = new Cell(changes.tail.x, changes.tail.y, CellState.empty);
                        refresh();
                        buffer = new Cell(changes.head.x, changes.head.y, CellState.snake);
                        refresh();
                    }
                    if (status) break;
                    refresh();
                }
            }
        }.start();
    }

    void refresh()
    {
        for (IPresenter presenter : list_players) {
            presenter.update();
        }
    }

    public Cell getBuffer() {
        System.out.printf("buffer x = %d, buffer y = %d, state = %s\n", buffer.x, buffer.y, buffer.state);
        return buffer;
    }

    public void addPresenter(IPresenter p) {
        list_players.add(p);
        gameInitialization(0);
        gameStart(0);
    }

    public void removePresenter(IPresenter p) {
        list_players.remove(p);
    }
}
