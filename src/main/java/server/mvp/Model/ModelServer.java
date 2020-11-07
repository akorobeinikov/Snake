package server.mvp.Model;

import java.util.ArrayList;
import java.util.Random;


import resources.Cell;
import resources.Game;
import server.mvp.Presenter.IPresenter;


class ModelServer implements IModelServer{

    ArrayList<Game> games = new ArrayList<>();
    ArrayList<IPresenter> list_players = new ArrayList<>();

    public ModelServer() {
        buffer = new Cell(0, 0, "empty");
        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                game_field[i][j] = new Cell(i, j, "empty");
            }
        }
    }

    public void setCell(Cell new_c) {
        game_field[new_c.x][new_c.y] = new_c;
        buffer = new_c;
        refresh();
    }

    public void generateNewItem() {
        int point = new Random().nextInt(height*width - filled);
        System.out.println(point);

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(game_field[i][j].isEmpty()) {
                    if (point == 0) {
                        game_field[i][j].state = "eat";
                        buffer = game_field[i][j];
                        refresh();
                        return;
                    }else {
                        point--;
                    }
                }
            }
        }
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
