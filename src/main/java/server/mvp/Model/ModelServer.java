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
    ArrayDeque<Integer> free_games = new ArrayDeque<>();
    ArrayList<Cell> buffer = new ArrayList<>();

    public ModelServer() {}

    public void generateNewItem(int game_id) {
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
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < game.snakes.length; ++i) {
                        if (game.snakes[i] == null) {
                            continue;
                        }
                        SnakeChanges changes = game.moveSnakeHead(i);
                        status = games.get(gameId).status;
                        if (status) {
                            buffer.set(gameId, new Cell(-1, 1 - i, CellState.fromInteger(2 + i)));
                            refresh(gameId);
                            System.out.println("GAME OVER " + gameId);
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

    public void stopGame(int p_id) {
        int game_id = getGameId(p_id);
        games.get(game_id).status = true;
    }

    void refresh(int game_id)
    {
        for (int i = 0; i < presenter_game.size(); i++) {
            if(getGameId(i) == game_id && list_players.get(i) != null) {// add additional ArrayList game_id->p_ids?
                list_players.get(i).update();
            }
        }
    }

    void refresh(int game_id, int p_id)
    {
        if (list_players.get(p_id) != null) {
            list_players.get(p_id).update();
        }
    }

    public Cell getBuffer(int p_id) {
        int game_id = getGameId(p_id);
        return buffer.get(game_id);
    }

    public void addPresenter(int p_id, IPresenter p) {
        System.out.println("Add " + p_id);
        if (p_id < list_players.size()) {
            list_players.set(p_id, p);
        } else {
            list_players.add(p);
        }
        System.out.println(free_games.toString());

        // logic connect player to game
        if (free_games.isEmpty()) {
            games.add(new Game());
            games.get(games.size() - 1).addPlayer(p_id);
            free_games.offerLast(games.size()-1);
            buffer.add(new Cell());
            if (p_id < presenter_game.size()) {
                presenter_game.set(p_id, games.size() - 1);
            } else {
                presenter_game.add(games.size() - 1);
            }
        } else {
            int gameId = free_games.pollFirst();
            games.get(gameId).addPlayer(p_id);
            if (p_id < presenter_game.size()) {
                presenter_game.set(p_id, gameId);
            } else {
                presenter_game.add(gameId);
            }
        }

        // game initialization (preparing to start)
        int game_id = getGameId(p_id);
        if (games.get(game_id).players.size() == 1) {
            generateNewItem(game_id);
            infoForFirstPlayer(p_id);
            addSnake(p_id);
            refresh(getGameId(p_id));
        } else {
            updateSecondPlayer(p_id);
            infoForSecondPlayer(p_id);
            addSnake(p_id);
            refresh(getGameId(p_id));
            gamePrepare(getGameId(p_id));
        }

        // print info about games on server
        System.out.println(list_players.toString());
        System.out.println(presenter_game.toString());
        System.out.println(games.toString());
        for (Game game:games) {
            System.out.println(game + ": ");
            for (int id: game.players) {
                System.out.print(id + " ");
            }
            System.out.println();
        }
    }

    public void updateSnakeDirection(int p_id, int direction) {
        int game_id = getGameId(p_id);
        int snake_index = games.get(game_id).getSnakeIndex(p_id);
        games.get(game_id).snakes[snake_index].changeDirection(direction);
    }

    public void gamePrepare(int game_id) {
        infoAboutStartGame(game_id);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        games.get(game_id).status = false;
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

    private void infoAboutRestartGame(int game_id) {
        buffer.set(game_id, new Cell(100, 1, CellState.empty));
        refresh(game_id);
    }

    public void removePresenter(int p_id) {
        System.out.println("Remove " + p_id);
        // add p_id to free_ids
        list_players.set(p_id, null);
        // game delete this player and add to free_games
        games.get(getGameId(p_id)).players.remove(Integer.valueOf(p_id));
        if (games.get(getGameId(p_id)).players.size() == 1) {
            int game_id = getGameId(p_id);
            // update game
            System.out.println("Game update!");
            int player = games.get(game_id).players.get(0);
            System.out.println(player);
            games.set(game_id, new Game());
            games.get(game_id).addPlayer(player);
            // time to stop last gameStart thread
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // add player to game
            infoAboutRestartGame(game_id);
            generateNewItem(game_id);
//            infoForFirstPlayer(player);
            addSnake(player);
            refresh(getGameId(player));
            free_games.offerLast(getGameId(p_id));
        } else {
            int game_id = getGameId(p_id);
            // rebuild game
            games.set(game_id, new Game());
            free_games.offerLast(game_id);
        }
        presenter_game.set(p_id, -1);

    }

    private int getGameId(int p_id) {
        if (p_id < presenter_game.size())
            return presenter_game.get(p_id);
        else
            return -1;
    }
}
