package server.mvp.Model;
import resources.Cell;
import server.mvp.Presenter.IPresenter;

public interface IModelServer {
    void generateNewItem(int p_id);
    Cell getBuffer(int p_id);
    void update(int p_id, int direction);
    void updateSnakeDirection(int p_id, int direction);
    void addPresenter(int p_id, IPresenter p);
    void removePresenter(int p_id);
    void stopGame(int p_id);
}
