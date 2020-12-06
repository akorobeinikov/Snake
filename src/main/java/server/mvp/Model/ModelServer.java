package server.mvp.Model;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import resources.Cell;
import resources.CellState;
import resources.Game;
import resources.SnakeChanges;
import server.mvp.Presenter.IPresenter;


class ModelServer implements IModelServer {

    ArrayList<Game> games = new ArrayList<>();
    ArrayList<IPresenter> list_players = new ArrayList<>();
    ArrayList<Integer> presenter_game = new ArrayList<>();
    ArrayList<Integer> presenter_snake = new ArrayList<>();
    ArrayDeque<Integer> free_games = new ArrayDeque<>();
    ArrayList<Cell> buffer = new ArrayList<>();

    public ModelServer() {}

    public void generateNewItem(int p_id) {
        int game_id = getGameId(p_id);
        buffer.set(game_id, games.get(game_id).generateNewItem()); // what if return is (-1, -1, -1)?
        refresh(game_id);
    }

    public void addSnake(int p_id) {
        ArrayList<Cell> snake = games.get(getGameId(p_id)).addSnake();
        for (int i = 0; i < snake.size(); ++i) {
            buffer.set(getGameId(p_id), snake.get(i));
            refresh(getGameId(p_id));
        }
    }

    public void gameStart(int gameId) {
        new Thread(){
            @Override
            public void run() {
                Game game = games.get(gameId);
                boolean status = false;
                boolean eaten = false;
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
                        SnakeChanges changes = game.moveSnakeHead(i);
                        status = changes.status;
                        System.out.printf("Status = %b \n", status);
                        if (status) {
                            buffer.set(gameId, new Cell(-1, 1 - i, CellState.fromInteger(2 + i)));
                            refresh(gameId);
                            System.out.println("GAME OVER");
                            break;
                        }
                        buffer.set(gameId, new Cell(changes.head.x, changes.head.y, CellState.fromInteger(2 + i)));
                        refresh(gameId);
                        if(changes.moving_to_tail) {
                            Point tail = game.moveSnakeTail(i);
                            continue;
                        }
                        if (changes.is_grow) {
                            eaten = true;
                            game.snakes[i].increaseSnake();
                        } else {
                            Point tail = game.moveSnakeTail(i);
                            buffer.set(gameId, new Cell(tail.x, tail.y, CellState.empty));
                            refresh(gameId);
                        }
                    }
                    if (status) break;
                    if(eaten == true) {
                        eaten = false;
                        generateNewItem(gameId);
                    }
                    refresh(gameId);
                }
            }
        }.start();
    }

    void refresh(int game_id)
    {
        for (int i = 0; i < presenter_game.size(); i++) {
            if(getGameId(i) == game_id) {// add additional ArrayList game_id->p_ids?
                list_players.get(i).update();
            }
        }
    }

    void refresh(int game_id, int p_id)
    {
        list_players.get(p_id).update();
    }

    public Cell getBuffer(int p_id) {
        int game_id = getGameId(p_id);
//        System.out.printf("buffer p_id = %d, game = %d, x = %d, y = %d, state = %s\n", p_id, game_id, buffer.get(game_id).x, buffer.get(game_id).y, buffer.get(game_id).state);
        return buffer.get(game_id);
    }

    public void addPresenter(int p_id, IPresenter p) {
        list_players.add(p_id, p);

        if (free_games.isEmpty()) {
            games.add(new Game());
            buffer.add(new Cell());
            presenter_game.add(p_id, games.size()-1);
            generateNewItem(p_id);
            infoForFirstPlayer(p_id);
            free_games.offerLast(games.size()-1);
            presenter_snake.add(p_id, 0);
            addSnake(p_id);
            refresh(getGameId(p_id));
        } else {
            int gameId = free_games.pollFirst();
            presenter_game.add(p_id, gameId);
            presenter_snake.add(p_id, 1);
            updateSecondPlayer(p_id);
            infoForSecondPlayer(p_id);
            addSnake(p_id);
            refresh(getGameId(p_id));
            gamePrepare(getGameId(p_id));
        }
    }

    public void updateSnakeDirection(int p_id, int direction) {
        games.get(getGameId(p_id)).snakes[presenter_snake.get(p_id)].changeDirection(direction);
    }

    public void gamePrepare(int game_id) {
        infoAboutStartGame(game_id);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameStart(game_id);
    }

    public void update(int p_id, int direction) {
        if (direction >= 0) {
            updateSnakeDirection(p_id, direction);
        }
    }

    private void updateSecondPlayer(int p_id) {
        ArrayList<Cell> cells = games.get(getGameId(p_id)).getNotEmptyCells();
        for(int i = 0; i < cells.size(); i++) {
            buffer.set(getGameId(p_id), cells.get(i));
            refresh(getGameId(p_id));
        }
    }

    private void infoForFirstPlayer(int p_id) {
        buffer.set(getGameId(p_id), new Cell(-2, 1, CellState.empty));
        refresh(getGameId(p_id), p_id);
    }

    private void infoForSecondPlayer(int p_id) {
        buffer.set(getGameId(p_id), new Cell(-2, 2, CellState.empty));
        refresh(getGameId(p_id), p_id);
    }

    private void infoAboutStartGame(int game_id) {
        buffer.set(game_id, new Cell(-2, 3, CellState.empty));
        refresh(game_id);
    }

    public void removePresenter(int p_id, IPresenter p) {
        list_players.remove(p);
        presenter_game.set(p_id, -1);
        // add game to free games?
        // need to remove snake of p_id and send update
    }

    private int getGameId(int p_id) {
        if (p_id < presenter_game.size())
            return presenter_game.get(p_id);
        else
            return -1;
    }
}
