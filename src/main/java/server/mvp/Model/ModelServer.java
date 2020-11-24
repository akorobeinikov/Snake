package server.mvp.Model;

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

    public void setCell(int p_id, Cell new_c) {
        int game_id = getGameId(p_id);
        games.get(game_id).setCell(new_c);
        buffer.set(game_id, new_c);
        refresh(game_id);
    }


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
                        buffer.set(gameId, new Cell(changes.tail.x, changes.tail.y, CellState.empty));
                        refresh(gameId);
                        buffer.set(gameId, new Cell(changes.head.x, changes.head.y, CellState.snake));
                        refresh(gameId);
                    }
                    if (status) break;
                    refresh(gameId);
                }
            }
        }.start();
    }

    void refresh(int game_id)
    {
        for (int i = 0; i < list_players.size(); i++) {
            if(getGameId(i) == game_id) {// add additional ArrayList game_id->p_ids?
                list_players.get(i).update();
            }
        }
    }

    public Cell getBuffer(int p_id) {
        int game_id = getGameId(p_id);
        System.out.printf("buffer p_id = %d, game = %d, x = %d, y = %d, state = %s\n", p_id, game_id, buffer.get(game_id).x, buffer.get(game_id).y, buffer.get(game_id).state);
        return buffer.get(game_id);
    }

    public void addPresenter(int p_id, IPresenter p) {
        list_players.add(p);

        if (free_games.isEmpty()) {
            games.add(new Game());
            buffer.add(new Cell());
            presenter_game.add(p_id, games.size()-1);
            generateNewItem(p_id);
            free_games.offerLast(games.size()-1);
        } else {
            presenter_game.add(p_id, free_games.pollFirst());
            updateSecondPlayer(p_id);
        }
        addSnake(p_id);
        refresh(getGameId(p_id));
    }

    private void updateSecondPlayer(int p_id) {
        ArrayList<Cell> cells = games.get(getGameId(p_id)).getNotEmptyCells();
        for(int i = 0; i < cells.size(); i++) {
            buffer.set(getGameId(p_id), cells.get(i));
            refresh(getGameId(p_id));
        }
    }

    public void removePresenter(int p_id, IPresenter p) {
        list_players.remove(p);
        presenter_game.set(p_id, -1);
        // add game to free games?
        // need to remove snake of p_id and send update
    }

    private int getGameId(int p_id) {
        return presenter_game.get(p_id);
    }
}
