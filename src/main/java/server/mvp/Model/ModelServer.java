package server.mvp.Model;

import java.util.ArrayList;
import java.util.Random;


import resources.Cell;
import resources.Game;
import server.mvp.Presenter.IPresenter;


class ModelServer implements IModelServer{

    ArrayList<Game> games = new ArrayList<>();
    ArrayList<IPresenter> list_players = new ArrayList<>();
    Cell buffer;
    public ModelServer() {
        games.add(new Game());
        buffer = new Cell(0, 0, "empty");
    }

    public void setCell(Cell new_c) {
        games.get(0).setCell(new_c);
        buffer = new_c;
        refresh();
    }

    public void generateNewItem() {
        buffer = games.get(0).generateNewItem(); // what if return is (-1, -1, -1)?
        refresh();
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
        generateNewItem();
        refresh();
    }

    public void removePresenter(IPresenter p) {
        list_players.remove(p);
    }
}
