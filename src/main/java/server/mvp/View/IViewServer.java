package server.mvp.View;


import resources.Cell;

public interface IViewServer {
    int getOp();
    void setCell(final Cell point);
    Cell getCell();
    void setOp(int op);
}
