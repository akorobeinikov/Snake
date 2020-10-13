package server.mvp.Model;
import resources.Cell;
import server.mvp.Presenter.IPresenter;

public interface IModelServer {
    void setCell(Cell new_c);
    void generateNewItem();
    Cell getBuffer();
    void addPresenter(IPresenter p);
    void removePresenter(IPresenter p);
}
