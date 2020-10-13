package server.mvp.View;

public interface IViewServer {
    int getOp();
    void setField(int[][] gameField);
    int[][] getField();
    void setOp(int op);
}
