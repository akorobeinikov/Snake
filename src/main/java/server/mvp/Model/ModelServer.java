package server.mvp.Model;

import java.util.ArrayList;
import java.util.Random;


import resources.Cell;
import server.mvp.Presenter.IPresenter;


class ModelServer implements IModelServer{
    String mes = "message not found!";
    int height = 20;
    int width = 20;
    int filled = 0;
    Cell buffer;
    Cell[][] game_field = new Cell[height][width];

    ArrayList<IPresenter> list_players = new ArrayList<>();

    public ModelServer() {
        buffer = new Cell(0, 0);
        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                game_field[i][j] = new Cell(i, j);
            }
        }
    }

    public void setCell(Cell new_c) {
        game_field[new_c.getX()][new_c.getY()] = new_c;
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
                        game_field[i][j].setState("eat");
                        buffer = game_field[i][j];
                        return;
                    }else {
                        point--;
                    }
                }
            }
        }
        refresh();
    }
    void refresh()
    {
        for (IPresenter presenter : list_players) {
            presenter.update();
        }
    }

    public Cell getBuffer() {
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
