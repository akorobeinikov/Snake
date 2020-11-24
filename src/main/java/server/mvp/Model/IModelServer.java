package server.mvp.Model;
import resources.Cell;
import server.mvp.Presenter.IPresenter;

public interface IModelServer {
    void setCell(int p_id, Cell new_c);
    void generateNewItem(int p_id);
    Cell getBuffer(int p_id);
    void addPresenter(int p_id, IPresenter p);
    void removePresenter(int p_id, IPresenter p);
}
